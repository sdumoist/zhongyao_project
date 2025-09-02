package com.xpxp.pojo.dto;

import com.xpxp.model.QueryValue;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 查询DTO类
 */
@Data
public class QueryDTO {
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 极性：1(正)/0(负)/null(所有)
     */
    private Integer polarity;
    
    /**
     * CAS号
     */
    private String cas;
    
    /**
     * 中文名称
     */
    private String cname;
    
    /**
     * 分子量
     */
    private String mw;
    
    /**
     * 分子式
     */
    private String formula;
    
    /**
     * 检测值
     */
    private BigDecimal value;
    
    /**
     * 检测值误差范围
     */
    private BigDecimal valueRange;
    
    /**
     * 二级质谱数据列表
     */
    private List<Ms2Data> ms2s;
    
    /**
     * 二级质谱数据内部类
     */
    @Data
    public static class Ms2Data {
        /**
         * 二级质谱数据值
         */
        private BigDecimal ms2;
        
        /**
         * 二级质谱数据误差范围
         */
        private BigDecimal ms2Range;
    }
    
    /**
     * 验证查询参数
     */
    public boolean isValid() {
        // 检查value和valueRange
        if (value != null && valueRange == null) {
            return false;
        }
        if (value == null && valueRange != null) {
            return false;
        }
        if (valueRange != null && valueRange.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        
        // 检查ms2s列表
        if (ms2s != null) {
            for (Ms2Data ms2Data : ms2s) {
                if (ms2Data.getMs2() == null || ms2Data.getMs2Range() == null) {
                    return false;
                }
                if (ms2Data.getMs2Range().compareTo(BigDecimal.ZERO) < 0) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 获取查询值数量（包括value和ms2s）
     */
    public int getQueryValueCount() {
        int count = 0;
        if (value != null) {
            count++;
        }
        if (ms2s != null) {
            count += ms2s.size();
        }
        return count;
    }
    
    /**
     * 检查是否有查询条件
     */
    public boolean hasQueryConditions() {
        return name != null || polarity != null || cas != null || cname != null || 
               mw != null || formula != null || value != null || 
               (ms2s != null && !ms2s.isEmpty());
    }
    
    /**
     * 获取所有查询值（用于匹配计算）
     */
    public List<QueryValue> getAllQueryValues() {
        List<QueryValue> queryValues = new java.util.ArrayList<>();
        
        // 添加value查询值
        if (value != null && valueRange != null) {
            QueryValue queryValue = new QueryValue();
            queryValue.setValue(value.doubleValue());
            queryValue.setTolerance(valueRange.doubleValue());
            queryValues.add(queryValue);
        }
        
        // 添加ms2s查询值
        if (ms2s != null) {
            for (Ms2Data ms2Data : ms2s) {
                QueryValue queryValue = new QueryValue();
                queryValue.setValue(ms2Data.getMs2().doubleValue());
                queryValue.setTolerance(ms2Data.getMs2Range().doubleValue());
                queryValues.add(queryValue);
            }
        }
        
        return queryValues;
    }
} 