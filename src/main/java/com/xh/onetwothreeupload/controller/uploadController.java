package com.xh.onetwothreeupload.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.xh.onetwothreeupload.dao.SaleDao;
import com.xh.onetwothreeupload.to.SaleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.List;

//@RestController
@Slf4j
public class uploadController {
    @Autowired
    private  SaleDao saleDao;

//    销售数据：
//    年： DSCN_ETMS Code_S_YYYYMMDDHHMMSS_YTD.csv
//
//    采购数据：
//    年： DSCN_ETMS Code_P_YYYYMMDDHHMMSS_YTD.csv
//
//    库存数据：
//    日： DSCN_ETMS Code_I_YYYYMMDDHHMMSS_Daily.csv

private static String SaleFileName="DSCN_C44000074_S_YYYYMMDDHHMMSS_YTD.csv";
private static String PurFileName="DSCN_C44000074_P_YYYYMMDDHHMMSS_YTD.csv";
private static String StoreFileName="DSCN_C44000074_I_YYYYMMDDHHMMSS_YTD.csv";
    public  void test(){
        String fileName = URLEncoder.encode(SaleFileName);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExcelWriter writer = new ExcelWriterBuilder()
                .autoCloseStream(true)
                .excelType(ExcelTypeEnum.XLSX)
                .file(out)
                .head(SaleDTO.class)
                .build();
        // xlsx文件上上限是104W行左右,这里如果超过104W需要分Sheet
        WriteSheet writeSheet = new WriteSheet();
        writeSheet.setSheetName("target");

        List<SaleDTO> list = getSaleDTO();
        writer.write(list, writeSheet);
        writer.finish();
        System.out.println(out);
//        FTPTools.upload("","");

    }

    private  List<SaleDTO> getSaleDTO() {
        List<SaleDTO> saleDTOS=saleDao.getSaleList();
        return saleDTOS;
    }


}

