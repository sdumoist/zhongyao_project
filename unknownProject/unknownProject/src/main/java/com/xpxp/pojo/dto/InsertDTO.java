package com.xpxp.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 化学化合物插入DTO
 */
@Data
public class InsertDTO {
    
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
    private String mw;
    
    /**
     * 检测值（逗号分隔的数值字符串）
     */
    private String value;
    
    /**
     * 二级质谱数据（逗号分隔的数值字符串）
     */
    private String ms2s;
    
    /**
     * 验证数据格式
     */
    public boolean isValid() {
        // 检查检测值格式
        if (value != null && !value.trim().isEmpty()) {
            if (!isValidNumberList(value)) {
                return false;
            }
        }
        
        // 检查二级质谱数据格式
        if (ms2s != null && !ms2s.trim().isEmpty()) {
            if (!isValidNumberList(ms2s)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 验证数值列表格式
     */
    private boolean isValidNumberList(String numberList) {
        if (numberList == null || numberList.trim().isEmpty()) {
            return true;
        }
        
        String[] numbers = numberList.split(",");
        for (String number : numbers) {
            try {
                new BigDecimal(number.trim());
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取检测值列表
     */
    public java.util.List<BigDecimal> getValueList() {
        if (value == null || value.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        return java.util.Arrays.stream(value.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(BigDecimal::new)
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 获取二级质谱数据列表
     */
    public java.util.List<BigDecimal> getMs2List() {
        if (ms2s == null || ms2s.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        return java.util.Arrays.stream(ms2s.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(BigDecimal::new)
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 设置检测值列表
     */
    public void setValueFromList(java.util.List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            this.value = null;
        } else {
            this.value = values.stream()
                .map(BigDecimal::toString)
                .collect(java.util.stream.Collectors.joining(","));
        }
    }
    
    /**
     * 设置二级质谱数据列表
     */
    public void setMs2FromList(java.util.List<BigDecimal> ms2Values) {
        if (ms2Values == null || ms2Values.isEmpty()) {
            this.ms2s = null;
        } else {
            this.ms2s = ms2Values.stream()
                .map(BigDecimal::toString)
                .collect(java.util.stream.Collectors.joining(","));
        }
    }
} 
 
 
 
 
 
 