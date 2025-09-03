// src/main/java/com/xpxp/pojo/vo/ToxicOneVO.java
package com.xpxp.pojo.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ToxicOneVO {
    private Long id;
    private String name;
    private String formula;
    private BigDecimal exactMw;
    private List<BigDecimal> fragments;  // 数据库中解析后的碎片列表
}