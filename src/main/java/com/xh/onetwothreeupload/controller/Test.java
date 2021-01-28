package com.xh.onetwothreeupload.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.xh.onetwothreeupload.config.FTPTools;
import com.xh.onetwothreeupload.dao.baseDao;
import com.xh.onetwothreeupload.to.PurDTO;
import com.xh.onetwothreeupload.to.SaleDTO;
import com.xh.onetwothreeupload.to.StoreDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 开启定时任务的注解
 */
@Component
@EnableScheduling
@Slf4j
public class Test {
    @Autowired
    private baseDao baseDao;

    @Autowired
    private ThreadPoolExecutor executor;

    @Scheduled(cron = "0 0 0 * * ?")
    public void job1() throws FileNotFoundException, ExecutionException, InterruptedException {

        //获取当前日期
        LocalDateTime ldt = LocalDateTime.now();

        String time = ldt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        //    销售数据：
//    年： DSCN_ETMS Code_S_YYYYMMDDHHMMSS_YTD.csv
//
//    采购数据：
//    年： DSCN_ETMS Code_P_YYYYMMDDHHMMSS_YTD.csv
//
//    库存数据：
//    日： DSCN_ETMS Code_I_YYYYMMDDHHMMSS_Daily.csv
        String saleFileName = "DSCN_C44000074_S_" + time + "_YTD.csv";
        String purFileName = "DSCN_C44000074_P_" + time + "_YTD.csv";
        String storeFileName = "DSCN_C44000074_I_" + time + "_YTD.csv";

        CompletableFuture<Void> saleFunture = CompletableFuture.runAsync(() -> {
            try {
                log.info("开始上传销售数据");
                getDataStreamAndUpload(saleFileName, baseDao.getSaleList(), SaleDTO.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.error("上传销售数据错误");
            }
        }, executor);

        CompletableFuture<Void> purFunture = CompletableFuture.runAsync(() -> {
            try {
                log.info("开始上传采购数据");
                getDataStreamAndUpload(purFileName, baseDao.getPurList(), PurDTO.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.error("上传采购数据错误");
            }
        }, executor);

        CompletableFuture<Void> storeFunture = CompletableFuture.runAsync(() -> {
            try {
                log.info("开始上传库存数据");
                getDataStreamAndUpload(storeFileName, baseDao.getStoreList(), StoreDTO.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.error("上传库存数据错误");
            }
        }, executor);

        CompletableFuture.allOf(saleFunture, purFunture, storeFunture).get();


    }

    /**
     * 获取文件流并上传
     *
     * @param fileName
     * @param data
     * @param clas     对应Excel头
     * @throws FileNotFoundException
     */
    private void getDataStreamAndUpload(String fileName, List data, Class clas) throws FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExcelWriter writer = new ExcelWriterBuilder()
                .autoCloseStream(true)
                .excelType(ExcelTypeEnum.XLSX)
                .file(out)
                .head(clas)
                .build();
        // xlsx文件上上限是104W行左右,这里如果超过104W需要分Sheet
        WriteSheet writeSheet = new WriteSheet();
        writeSheet.setSheetName("target");
        writer.write(data, writeSheet);
        writer.finish();

//                write(new ByteArrayInputStream(out.toByteArray()),"C:/Users/Administrator/Desktop/新建文件夹",fileName);
//ftp上传
        FTPTools.upload(new ByteArrayInputStream(out.toByteArray()), fileName);
    }


    public void write(InputStream is, String path, String fileName) {
        try {


            File filedir = new File(path);
            if (!filedir.exists())
                filedir.mkdirs();
            File file = new File(filedir, fileName);
            OutputStream os = new FileOutputStream(file);

            byte[] byteStr = new byte[1024];
            int len = 0;
            while ((len = is.read(byteStr)) > 0) {
                os.write(byteStr, 0, len);
            }
            is.close();

            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
