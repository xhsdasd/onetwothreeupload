package com.xh.onetwothreeupload.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.onetwothreeupload.to.hq.FlowOrSaleDTO;
import com.xh.onetwothreeupload.to.hq.PurDTO;
import com.xh.onetwothreeupload.to.hq.StoreDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Mapper
public interface HQDao extends BaseMapper<T> {


    List<FlowOrSaleDTO> getFlowList(@Param("type") String type);

    List<PurDTO> getPurList();

    List<StoreDTO> getStockList();
}
