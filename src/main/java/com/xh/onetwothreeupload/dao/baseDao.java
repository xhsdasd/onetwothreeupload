package com.xh.onetwothreeupload.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.onetwothreeupload.to.PurDTO;
import com.xh.onetwothreeupload.to.SaleDTO;
import com.xh.onetwothreeupload.to.StoreDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Mapper
public interface baseDao extends BaseMapper<T> {

    List<SaleDTO> getSaleList();
    List<PurDTO> getPurList();
    List<StoreDTO> getStoreList();
    void updateSale();
    void updatePur();

}