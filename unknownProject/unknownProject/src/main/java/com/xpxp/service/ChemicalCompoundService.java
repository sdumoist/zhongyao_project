package com.xpxp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xpxp.pojo.dto.InsertDTO;
import com.xpxp.pojo.dto.QueryDTO;
import com.xpxp.pojo.vo.QueryVO;
import com.xpxp.pojo.entity.ChemicalCompound;

import java.util.List;

public interface ChemicalCompoundService extends IService<ChemicalCompound> {
    
    /**
     * 根据查询条件搜索化学化合物
     */
    List<QueryVO> searchByQuery(QueryDTO queryDTO);
    
    /**
     * 插入化学化合物
     */
    boolean insertChemicalCompound(InsertDTO insertDTO);
}
