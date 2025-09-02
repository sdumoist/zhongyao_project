package com.xpxp.controller;

import com.xpxp.pojo.dto.InsertDTO;
import com.xpxp.pojo.dto.QueryDTO;
import com.xpxp.pojo.vo.QueryVO;
import com.xpxp.model.ResponseResult;
import com.xpxp.service.ChemicalCompoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chemicalCompound")
public class ChemicalCompoundController {

    @Autowired
    private ChemicalCompoundService chemicalCompoundService;

    /**
     * 搜索化学化合物
     */
    @PostMapping("/search")
    public ResponseResult<List<QueryVO>> searchByQueryValues(@RequestBody QueryDTO queryDTO) {
        try {
            List<QueryVO> results = chemicalCompoundService.searchByQuery(queryDTO);
            return ResponseResult.success(results);
        } catch (Exception e) {
            return ResponseResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 插入化学化合物
     */
    @PostMapping("/insert")
    public ResponseResult<String> insertChemicalCompound(@RequestBody InsertDTO insertDTO) {
        try {
            // 验证必填字段
            if (insertDTO.getName() == null || insertDTO.getName().trim().isEmpty()) {
                return ResponseResult.error("名称不能为空");
            }
            
            // 验证数据格式
            if (!insertDTO.isValid()) {
                return ResponseResult.error("数据格式不正确，请检查数值字段");
            }
            
            // 执行插入
            boolean success = chemicalCompoundService.insertChemicalCompound(insertDTO);
            
            if (success) {
                return ResponseResult.success("插入成功");
            } else {
                return ResponseResult.error("插入失败：数据已存在（名称、CAS号和极性都相同的记录已存在）");
            }
            
        } catch (Exception e) {
            return ResponseResult.error("插入失败: " + e.getMessage());
        }
    }
}
