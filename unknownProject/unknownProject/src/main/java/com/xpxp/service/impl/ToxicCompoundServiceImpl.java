// src/main/java/com/xpxp/service/impl/ToxicCompoundServiceImpl.java
package com.xpxp.service.impl;

import com.xpxp.mapper.ToxicCompoundMapper;
import com.xpxp.pojo.dto.ToxicSearchOneDTO;
import com.xpxp.pojo.entity.ToxicCompound;
import com.xpxp.pojo.vo.ToxicOneVO;
import com.xpxp.service.ToxicCompoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ToxicCompoundServiceImpl implements ToxicCompoundService {

    private final ToxicCompoundMapper mapper;

    @Override
    public ToxicOneVO searchOne(ToxicSearchOneDTO dto) {
        // 1) SQL 初筛
        var candidates = mapper.searchByFilter(
                dto.getName(),
                dto.getFormula(),
                dto.getExactMw(),
                dto.getExactMwRange(),
                200
        );

        if (candidates == null || candidates.isEmpty()) return null;

        // 2) Java 端按碎片 ±误差做二次匹配（要求全部命中）
        for (ToxicCompound c : candidates) {
            List<BigDecimal> frags = parseFragments(c.getFragments());
            if (matchesAll(dto.getMs2s(), frags)) {
                return toVO(c, frags);
            }
        }
        return null;
    }

    private List<BigDecimal> parseFragments(String s) {
        if (s == null || s.isBlank()) return Collections.emptyList();
        return Arrays.stream(s.split("[,;\\s]+"))
                .filter(t -> !t.isBlank())
                .map(BigDecimal::new)
                .collect(Collectors.toList());
    }

    private boolean matchesAll(List<ToxicSearchOneDTO.Ms2> req, List<BigDecimal> candidate) {
        if (req == null || req.isEmpty()) return true; // 没传碎片就只按 SQL 条件
        for (ToxicSearchOneDTO.Ms2 m : req) {
            BigDecimal mz  = m.getMz();
            BigDecimal tol = (m.getMzRange() == null) ? new BigDecimal("0.01") : m.getMzRange();
            boolean hit = candidate.stream().anyMatch(x -> x.subtract(mz).abs().compareTo(tol) <= 0);
            if (!hit) return false;
        }
        return true;
    }

    private ToxicOneVO toVO(ToxicCompound c, List<BigDecimal> frags) {
        ToxicOneVO vo = new ToxicOneVO();
        vo.setId(c.getId());
        vo.setName(c.getName());
        vo.setFormula(c.getFormula());
        vo.setExactMw(c.getExactMw());
        vo.setFragments(frags);
        return vo;
    }
}