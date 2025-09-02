package com.xpxp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xpxp.pojo.entity.ChemicalCompound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 化学物质信息 Mapper
 */
@Mapper
public interface ChemicalCompoundMapper extends BaseMapper<ChemicalCompound> {
    
    /**
     * 根据名称查询
     */
    @Select("SELECT * FROM chemical_compounds WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<ChemicalCompound> findByName(@Param("name") String name);
    
    /**
     * 根据极性查询
     */
    @Select("SELECT * FROM chemical_compounds WHERE polarity = #{polarity}")
    List<ChemicalCompound> findByPolarity(@Param("polarity") Integer polarity);
    
    /**
     * 查询正极性数据
     */
    @Select("SELECT * FROM chemical_compounds WHERE polarity = 1")
    List<ChemicalCompound> findPositivePolarity();
    
    /**
     * 查询负极性数据
     */
    @Select("SELECT * FROM chemical_compounds WHERE polarity = 0")
    List<ChemicalCompound> findNegativePolarity();
    
    /**
     * 查询未知极性数据
     */
    @Select("SELECT * FROM chemical_compounds WHERE polarity IS NULL")
    List<ChemicalCompound> findUnknownPolarity();
    
    /**
     * 根据CAS号查询
     */
    @Select("SELECT * FROM chemical_compounds WHERE cas = #{cas}")
    List<ChemicalCompound> findByCas(@Param("cas") String cas);
    

    /**
     * 根据检测值范围查询
     */
    @Select("SELECT * FROM chemical_compounds WHERE value BETWEEN #{minValue} AND #{maxValue}")
    List<ChemicalCompound> findByValueRange(@Param("minValue") BigDecimal minValue, @Param("maxValue") BigDecimal maxValue);
}