// src/main/java/com/xpxp/controller/ToxicCompoundController.java
package com.xpxp.controller;

import com.xpxp.pojo.dto.ToxicSearchOneDTO;
import com.xpxp.pojo.vo.ToxicOneVO;
import com.xpxp.result.Result;
import com.xpxp.service.ToxicCompoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/toxic")
@RequiredArgsConstructor
public class ToxicCompoundController {

    private final ToxicCompoundService toxicCompoundService;

    @PostMapping("/searchOne")
    public Result<ToxicOneVO> searchOne(@RequestBody ToxicSearchOneDTO dto) {
        ToxicOneVO vo = toxicCompoundService.searchOne(dto);
        // 你的 Result 里如果没有 failure(...)，就用 error(...) 或自己在 Result 里加一个 failure(...) 别名
        return (vo == null) ? Result.error("未找到匹配化合物") : Result.success(vo);
    }
}