package com.xpxp.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 化学物质信息实体
 */
@TableName("chemical_compounds")
@Data
public class ChemicalCompound {
    
    @TableId(type = IdType.AUTO)
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
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 极性常量
     */
    public static class Polarity {
        public static final Integer POSITIVE = 1;  // 正
        public static final Integer NEGATIVE = 0;  // 负
        public static final Integer UNKNOWN = null; // 未知
    }
    
    /**
     * 获取极性描述
     */
    public String getPolarityDescription() {
        if (polarity == null) {
            return "未知";
        }
        return polarity == 1 ? "正" : "负";
    }
    
    /**
     * 设置极性
     */
    public void setPolarityByDescription(String description) {
        if ("正".equals(description)) {
            this.polarity = 1;
        } else if ("负".equals(description)) {
            this.polarity = 0;
        } else {
            this.polarity = null;
        }
    }
}