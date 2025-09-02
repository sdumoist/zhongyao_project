package com.xpxp.pojo.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询结果VO类
 */
@Data
public class QueryVO {

    /**
     * 记录ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 极性：1(正)/0(负)/null(未知)
     */
    private Integer polarity;

    /**
     * 极性描述
     */
    private String polarityDescription;

    /**
     * 中文名称
     */
    private String cname;

    /**
     * 分子式
     */
    private String formula;

    /**
     * CAS号
     */
    private String cas;

    /**
     * 理论分子量
     */
    private BigDecimal mw;

    /**
     * 检测值（逗号分隔的数值字符串）
     */
    private String value;

    /**
     * 二级质谱数据
     */
    private String ms2;


}