// src/main/java/com/xpxp/pojo/dto/ToxicSearchOneDTO.java
package com.xpxp.pojo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ToxicSearchOneDTO {
    private String name;                 // 可选：名称模糊
    private String formula;              // 可选：分子式精确
    private BigDecimal exactMw;          // 可选：精确分子量
    private BigDecimal exactMwRange;     // 可选：分子量允许误差（Da），如 0.01

    private List<Ms2> ms2s;              // 可选：特征离子碎片列表

    @Data
    public static class Ms2 {
        private BigDecimal mz;           // 目标 m/z
        private BigDecimal mzRange;      // 允许误差（Da），如 0.01
    }
}