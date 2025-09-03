package com.xpxp.pojo.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ToxicCompound {
    private Long id;
    private String name;              // 化合物名称
    private String formula;           // 分子式
    private BigDecimal exactMw;       // 精确分子量
    private String fragments;         // 碎片，存储为逗号分隔的字符串
}