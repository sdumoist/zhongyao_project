package com.xpxp.model;

import lombok.Data;

/**
 * 查询值模型
 */
@Data
public class QueryValue {
    /**
     * 查询值
     */
    private Double value;

    /**
     * 误差范围
     */
    private Double tolerance;
}





