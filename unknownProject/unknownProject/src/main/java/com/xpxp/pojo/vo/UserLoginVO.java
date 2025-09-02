package com.xpxp.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private String role;
    private String token;
    private Long id;
    private String avatar;
}
