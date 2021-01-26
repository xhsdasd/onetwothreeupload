package com.xh.onetwothreeupload.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.xh.onetwothreeupload.dao.baseDao;
import com.xh.onetwothreeupload.to.PurDTO;
import com.xh.onetwothreeupload.to.SaleDTO;
import com.xh.onetwothreeupload.to.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Component
/**
 * 开启定时任务的注解
 */
@EnableScheduling
public class Test {
@Autowired
private baseDao baseDao;

@Autowired
private ThreadPoolExecutor executor;

    @Scheduled(fixedRate = 1000*60*50)
    public void job1() throws FileNotFoundException, ExecutionException, InterruptedException {

       //获取当前日期
        LocalDateTime ldt = LocalDateTime.now();

        String time=ldt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        //    销售数据：
//    年： DSCN_ETMS Code_S_YYYYMMDDHHMMSS_YTD.csv
//
//    采购数据：
//    年： DSCN_ETMS Code_P_YYYYMMDDHHMMSS_YTD.csv
//
//    库存数据：
//    日： DSCN_ETMS Code_I_YYYYMMDDHHMMSS_Daily.csv
        String saleFileName="DSCN_C44000074_S_"+time+"_YTD.csv";
        String purFileName="DSCN_C44000074_P_"+time+"_YTD.csv";
        String storeFileName="DSCN_C44000074_I_"+time+"_YTD.csv";

        CompletableFuture<Void> saleFunture = CompletableFuture.runAsync(() -> {
            try {
                getDataStreamAndUpload(saleFileName, baseDao.getSaleList(), SaleDTO.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        },executor);

        CompletableFuture<Void> purFunture = CompletableFuture.runAsync(() -> {
            try {
                getDataStreamAndUpload(purFileName, baseDao.getPurList(),PurDTO.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        },executor);

        CompletableFuture<Void> storeFunture = CompletableFuture.runAsync(() -> {
            try {
                getDataStreamAndUpload(storeFileName, baseDao.getStoreList(),StoreDTO.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        },executor);

        CompletableFuture.allOf(saleFunture, purFunture, storeFunture).get();


    }

    /**
     * 获取文件流并上传
     * @param fileName
     * @param data
     * @param flag true为库存数据
     * @throws FileNotFoundException
     */
    private void getDataStreamAndUpload(String fileName, List data, Class clas) throws FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExcelWriter writer =new ExcelWriterBuilder()
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

                write(new ByteArrayInputStream(out.toByteArray()),"C:/Users/Administrator/Desktop/新建文件夹",fileName);
//ftp上传
//        FTPTools.upload(new ByteArrayInputStream(out.toByteArray()), saleFileName);
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void job2(){
        System.out.println("定时任务2" + new Date());
    }


    public  void write(InputStream is,String path,String fileName) {
        try {


            File filedir = new File(path);
            if (!filedir.exists())
                filedir.mkdirs();
            File file = new File(filedir, fileName);
            OutputStream os = new FileOutputStream(file);

            byte[] byteStr = new byte[1024];
            int len = 0;
            while ((len = is.read(byteStr)) > 0) {
                os.write(byteStr,0,len);
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