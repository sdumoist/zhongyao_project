package com.xpxp.controller;

import com.xpxp.Constant.ErrorConstant;
import com.xpxp.pojo.dto.LoginDTO;
import com.xpxp.pojo.dto.RegisterDTO;
import com.xpxp.pojo.vo.UserLoginVO;
import com.xpxp.result.Result;
import com.xpxp.service.AuthService;
import com.xpxp.service.CustomUserDetails;
import com.xpxp.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.GrantedAuthority;
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "登录接口控制器",description = "登录相关接口的集成")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录获取Token")
    @ApiResponse(responseCode = "200", description = "登录成功")
    @ApiResponse(responseCode = "401", description = "用户名或密码错误")
    public Result<UserLoginVO> login(@RequestBody LoginDTO loginDTO) {
        try {
            // 认证用户名和密码
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword())
            );

            // 获取 CustomUserDetails
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 获取用户 ID
            Long userId = userDetails.getId();
            String avatar = userDetails.getAvatar();

            // 获取用户角色
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            // 生成Token
            String token = jwtUtils.generateToken(loginDTO.getUsername(), role);

            return Result.success(new UserLoginVO(
                    loginDTO.getUsername(),
                    role.replace("ROLE_", ""),
                    token,
                    userId,
                    avatar
            ));
        } catch (AuthenticationException e) {
            return Result.error(ErrorConstant.LOGIN_FAILED, "用户名或密码错误");
        }
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册")
    @ApiResponse(responseCode = "200", description = "注册成功")
    @ApiResponse(responseCode = "400", description = "无效请求：用户名已存在/密码不符合要求/邮箱格式错误等")
    @ApiResponse(responseCode = "409", description = "资源冲突：用户名或邮箱已被注册")
    public Result register(@RequestBody RegisterDTO RejisterDTO) {
        authService.register(RejisterDTO);
        return Result.success();
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "重置密码", description = "用户忘记密码后重置")
    @ApiResponse(responseCode = "200", description = "重置密码成功")
    public Result resetPassword(@RequestBody RegisterDTO RejisterDTO) {
        authService.resetPassword(RejisterDTO);
        return Result.success();
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "用户退出登录")
    @ApiResponse(responseCode = "200", description = "用户注销成功")
    public Result logout() {
        return Result.success();
    }

}
