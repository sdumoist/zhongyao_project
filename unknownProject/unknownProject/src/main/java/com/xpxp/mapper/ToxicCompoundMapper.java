// src/main/java/com/xpxp/mapper/ToxicCompoundMapper.java
package com.xpxp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xpxp.pojo.entity.ToxicCompound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ToxicCompoundMapper extends BaseMapper<ToxicCompound> {

    @Select({
            "<script>",
            "SELECT id, name, formula, exact_mw AS exactMw, fragments",
            "FROM toxic_compounds",
            "<where>",
            "  <if test='name != null and name != \"\"'>",
            "    AND name LIKE CONCAT('%', #{name}, '%')",
            "  </if>",
            "  <if test='formula != null and formula != \"\"'>",
            "    AND formula = #{formula}",
            "  </if>",
            "  <if test='exactMw != null'>",
            "    <choose>",
            "      <when test='mwRange != null'>",
            "        AND exact_mw BETWEEN (#{exactMw} - #{mwRange}) AND (#{exactMw} + #{mwRange})",
            "      </when>",
            "      <otherwise>",
            "        AND ABS(exact_mw - #{exactMw}) &lt;= 0.0001",
            "      </otherwise>",
            "    </choose>",
            "  </if>",
            "</where>",
            "ORDER BY id",
            "LIMIT #{limit}",
            "</script>"
    })
    List<ToxicCompound> searchByFilter(@Param("name") String name,
                                       @Param("formula") String formula,
                                       @Param("exactMw") BigDecimal exactMw,
                                       @Param("mwRange") BigDecimal mwRange,
                                       @Param("limit") Integer limit);
}