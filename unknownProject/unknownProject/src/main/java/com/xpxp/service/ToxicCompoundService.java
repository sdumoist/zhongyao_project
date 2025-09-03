package com.xpxp.service;

import com.xpxp.pojo.dto.ToxicSearchOneDTO;
import com.xpxp.pojo.vo.ToxicOneVO;

public interface ToxicCompoundService {

    /**
     * 单个查询：按名称/分子式/分子量(±误差)初筛后，
     * 再按“特征离子碎片 ± 误差”在内存里逐个比对，返回第一条匹配结果
     */
    ToxicOneVO searchOne(ToxicSearchOneDTO dto);
}