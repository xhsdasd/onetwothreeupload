package com.xh.onetwothreeupload.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.onetwothreeupload.to.SaleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SaleDao extends BaseMapper<SaleDTO> {

    List<SaleDTO> getSaleList();

}