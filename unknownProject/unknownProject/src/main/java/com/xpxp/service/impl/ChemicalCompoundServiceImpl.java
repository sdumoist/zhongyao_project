package com.xpxp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xpxp.pojo.dto.InsertDTO;
import com.xpxp.pojo.dto.QueryDTO;
import com.xpxp.pojo.vo.QueryVO;
import com.xpxp.pojo.entity.ChemicalCompound;
import com.xpxp.mapper.ChemicalCompoundMapper;
import com.xpxp.service.ChemicalCompoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChemicalCompoundServiceImpl extends ServiceImpl<ChemicalCompoundMapper, ChemicalCompound> implements ChemicalCompoundService {

    @Autowired
    private ChemicalCompoundMapper chemicalCompoundMapper;

    @Override
    public List<QueryVO> searchByQuery(QueryDTO queryDTO) {
        // 1. 构建基础查询条件
        LambdaQueryWrapper<ChemicalCompound> wrapper = new LambdaQueryWrapper<>();
        
        // 2. 添加文本查询条件（如果参数有值）
        if (StringUtils.hasText(queryDTO.getName())) {
            wrapper.like(ChemicalCompound::getName, queryDTO.getName());
        }
        
        if (queryDTO.getPolarity() != null) {
            wrapper.eq(ChemicalCompound::getPolarity, queryDTO.getPolarity());
        }
        
        if (StringUtils.hasText(queryDTO.getCas())) {
            wrapper.eq(ChemicalCompound::getCas, queryDTO.getCas());
        }
        
        if (StringUtils.hasText(queryDTO.getCname())) {
            wrapper.like(ChemicalCompound::getCname, queryDTO.getCname());
        }
        
        if (StringUtils.hasText(queryDTO.getMw())) {
            wrapper.like(ChemicalCompound::getMw, queryDTO.getMw());
        }
        
        if (StringUtils.hasText(queryDTO.getFormula())) {
            wrapper.like(ChemicalCompound::getFormula, queryDTO.getFormula());
        }
        
        // 3. 执行基础查询
        List<ChemicalCompound> compounds = list(wrapper);
        
        // 4. 进行数值匹配过滤
        List<QueryVO> results = new ArrayList<>();
        
        for (ChemicalCompound compound : compounds) {
            // 检查是否匹配所有查询条件
            if (matchesQueryConditions(compound, queryDTO)) {
                QueryVO queryVO = convertToQueryVO(compound, queryDTO);
                results.add(queryVO);
            }
        }
        
        return results;
    }
    
    @Override
    public boolean insertChemicalCompound(InsertDTO insertDTO) {
        try {
            // 1. 验证数据格式
            if (!insertDTO.isValid()) {
                return false;
            }
            
            // 2. 检查是否已存在（根据名称、CAS号和极性）
            LambdaQueryWrapper<ChemicalCompound> wrapper = new LambdaQueryWrapper<>();
            
            // 构建查询条件：名称、CAS号和极性都相同才算重复
            if (StringUtils.hasText(insertDTO.getName())) {
                wrapper.eq(ChemicalCompound::getName, insertDTO.getName());
            }
            if (StringUtils.hasText(insertDTO.getCas())) {
                wrapper.eq(ChemicalCompound::getCas, insertDTO.getCas());
            }
            if (insertDTO.getPolarity() != null) {
                wrapper.eq(ChemicalCompound::getPolarity, insertDTO.getPolarity());
            }
            
            // 如果名称、CAS号和极性都为空，则无法判断重复
            if (!StringUtils.hasText(insertDTO.getName()) && 
                !StringUtils.hasText(insertDTO.getCas()) && 
                insertDTO.getPolarity() == null) {
                return false;
            }
            
            // 检查是否已存在（三个字段都相同才算重复）
            ChemicalCompound existing = getOne(wrapper);
            if (existing != null) {
                System.err.println("插入失败：数据已存在 - 名称:" + insertDTO.getName() + 
                                 ", CAS:" + insertDTO.getCas() + 
                                 ", 极性:" + insertDTO.getPolarity());
                return false; // 已存在，插入失败
            }
            
            // 3. 创建新的化学化合物实体
            ChemicalCompound compound = new ChemicalCompound();
            compound.setName(insertDTO.getName());
            compound.setPolarity(insertDTO.getPolarity());
            compound.setCname(insertDTO.getCname());
            compound.setFormula(insertDTO.getFormula());
            compound.setCas(insertDTO.getCas());
            // 处理mw字段，如果是数字字符串则转换为BigDecimal，否则保持为null
            if (StringUtils.hasText(insertDTO.getMw())) {
                try {
                    // 尝试提取数字部分（去掉括号等）
                    String mwStr = insertDTO.getMw().replaceAll("[^0-9.]", "");
                    if (!mwStr.isEmpty()) {
                        compound.setMw(new BigDecimal(mwStr));
                    }
                } catch (NumberFormatException e) {
                    // 如果转换失败，保持为null
                    System.err.println("分子量格式错误: " + insertDTO.getMw());
                }
            }
            compound.setValue(insertDTO.getValue());
            compound.setMs2(insertDTO.getMs2s()); // 注意：DTO中是ms2s，实体中是ms2
            
            // 4. 保存到数据库
            return save(compound);
            
        } catch (Exception e) {
            // 记录异常日志
            System.err.println("插入化学化合物失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 检查化合物是否匹配查询条件
     */
    private boolean matchesQueryConditions(ChemicalCompound compound, QueryDTO queryDTO) {
        // 检查检测值匹配
        if (queryDTO.getValue() != null && queryDTO.getValueRange() != null) {
            if (!isValueMatched(queryDTO.getValue(), queryDTO.getValueRange(), compound.getValue())) {
                return false;
            }
        }
        
        // 检查二级质谱数据匹配
        if (queryDTO.getMs2s() != null && !queryDTO.getMs2s().isEmpty()) {
            if (!isMs2Matched(queryDTO.getMs2s(), compound.getMs2())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 检查检测值是否匹配
     */
    private boolean isValueMatched(BigDecimal queryValue, BigDecimal tolerance, String dbValue) {
        if (dbValue == null || dbValue.trim().isEmpty()) {
            return false;
        }
        
        // 解析数据库中的检测值列表
        List<BigDecimal> dbValues = parseValueList(dbValue);
        
        // 检查是否有任何一个值在范围内
        return dbValues.stream().anyMatch(dbVal -> {
            BigDecimal diff = dbVal.subtract(queryValue).abs();
            return diff.compareTo(tolerance) <= 0;
        });
    }
    
    /**
     * 检查二级质谱数据是否匹配
     */
    private boolean isMs2Matched(List<QueryDTO.Ms2Data> queryMs2s, String dbMs2) {
        if (dbMs2 == null || dbMs2.trim().isEmpty()) {
            return false;
        }
        
        // 解析数据库中的二级质谱数据
        List<BigDecimal> dbMs2Values = parseValueList(dbMs2);
        
        // 检查所有查询的二级质谱数据是否都能匹配
        return queryMs2s.stream().allMatch(queryMs2 -> {
            return dbMs2Values.stream().anyMatch(dbMs2Val -> {
                BigDecimal diff = dbMs2Val.subtract(queryMs2.getMs2()).abs();
                return diff.compareTo(queryMs2.getMs2Range()) <= 0;
            });
        });
    }
    
    /**
     * 解析逗号分隔的数值字符串
     */
    private List<BigDecimal> parseValueList(String valueString) {
        if (valueString == null || valueString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return java.util.Arrays.stream(valueString.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(BigDecimal::new)
            .collect(Collectors.toList());
    }
    
    /**
     * 转换为QueryVO
     */
    private QueryVO convertToQueryVO(ChemicalCompound compound, QueryDTO queryDTO) {
        QueryVO queryVO = new QueryVO();
        
        // 设置基本信息
        queryVO.setId(compound.getId());
        queryVO.setName(compound.getName());
        queryVO.setPolarity(compound.getPolarity());
        queryVO.setCname(compound.getCname());
        queryVO.setFormula(compound.getFormula());
        queryVO.setCas(compound.getCas());
        queryVO.setMw(compound.getMw());
        queryVO.setValue(compound.getValue());
        queryVO.setMs2(compound.getMs2());
        
        return queryVO;
    }
    
    /**
     * 查找匹配的数值
     */
    private BigDecimal findMatchedValue(BigDecimal queryValue, BigDecimal tolerance, List<BigDecimal> dbValues) {
        return dbValues.stream()
            .filter(dbVal -> {
                BigDecimal diff = dbVal.subtract(queryValue).abs();
                return diff.compareTo(tolerance) <= 0;
            })
            .min((a, b) -> {
                BigDecimal diffA = a.subtract(queryValue).abs();
                BigDecimal diffB = b.subtract(queryValue).abs();
                return diffA.compareTo(diffB);
            })
            .orElse(null);
    }
}
