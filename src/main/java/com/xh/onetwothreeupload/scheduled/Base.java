package com.xh.onetwothreeupload.scheduled;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.xh.onetwothreeupload.config.FTPTools;
import com.xh.onetwothreeupload.dao.HQDao;
import com.xh.onetwothreeupload.dao.OneThreeDao;
import com.xh.onetwothreeupload.to.HostVo;
import com.xh.onetwothreeupload.to.hq.FlowOrSaleDTO;
import com.xh.onetwothreeupload.to.oneThree.PurDTO;
import com.xh.onetwothreeupload.to.oneThree.SaleDTO;
import com.xh.onetwothreeupload.to.oneThree.StoreDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
@Lazy(false)
public class Base {
    @Autowired
    private OneThreeDao OneThreeDao;
    @Autowired
    private HQDao HQDao;

    @Autowired
    private ThreadPoolExecutor executor;

    @Scheduled(cron = " 3 56 8 * * ? ")
//    @Scheduled(fixedRate = 1000*60*60*24)
    /**
     * 第一三共上传接口
     */
    public void oneThree() throws ExecutionException, InterruptedException {

        //    销售数据：
//    年： DSCN_ETMS Code_S_YYYYMMDDHHMMSS_YTD.csv
//
//    采购数据：
//    年： DSCN_ETMS Code_P_YYYYMMDDHHMMSS_YTD.csv
//
//    库存数据：
//    日： DSCN_ETMS Code_I_YYYYMMDDHHMMSS_Daily.csv
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String saleFileName = "DSCN_C44000074_S_" + time + "_YTD.csv";
        String purFileName = "DSCN_C44000074_P_" + time + "_YTD.csv";
        String storeFileName = "DSCN_C44000074_I_" + time + "_Daily.csv";
        HostVo hostVo = new HostVo("DSCN.pharmeyes.com", 40000, "C44000074", "DSCNC44000074!#$");
        CompletableFuture<Void> saleFunture = CompletableFuture.runAsync(() -> {
            try {
                List<SaleDTO> saleList = OneThreeDao.getSaleList();
                getDataStreamAndUpload(saleFileName, saleList, SaleDTO.class, hostVo);
                if (saleList != null && saleList.size() > 0) {
                    OneThreeDao.updateSale();
                }
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
                log.error("上传第一三共销售数据错误");
            }
        }, executor);

        CompletableFuture<Void> purFunture = CompletableFuture.runAsync(() -> {
            try {
                List<PurDTO> purList = OneThreeDao.getPurList();
                getDataStreamAndUpload(purFileName, purList, PurDTO.class, hostVo);
                if (purList != null && purList.size() > 0) {
                    OneThreeDao.updatePur();
                }

            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
                log.error("上传第一三共采购数据错误");
            }
        }, executor);

        CompletableFuture<Void> storeFunture = CompletableFuture.runAsync(() -> {
            try {
                List<StoreDTO> storeList = OneThreeDao.getStoreList();

                getDataStreamAndUpload(storeFileName, storeList, StoreDTO.class, hostVo);

            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
                log.error("上传第一三共库存数据错误");
            }
        }, executor);

        CompletableFuture.allOf(saleFunture, purFunture, storeFunture).get();


    }


    /**
     * 沈阳红旗上传接口
     */
    @Scheduled(cron = " 0 0 3 ? * 1,2,3,4,5 ")
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
//    @Scheduled(cron = " 3 56 10 * * ? ")
    public void syhq() throws ExecutionException, InterruptedException {

//    *命名规范：（说明：公司名称指的是广东广集医药有限公司）
//        流向：公司名称_流向_yyyymmdd.xls
//        60天销售：公司名称_60天销售_yyyymmdd.xls
//        采购：公司名称_购进_yyyymmdd.xls
//        库存：公司名称_库存_yyyymmdd.xls
//        样例：广东广集医药有限公司_流向_20200521.xls
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String flowFileName = "广东广集医药有限公司_流向_" + time + ".xls";
        String sixtySaleFileName = "广东广集医药有限公司_60天销售_" + time + ".xls";
        String purFileName = "广东广集医药有限公司_购进_" + time + ".xls";
        String storeFileName = "广东广集医药有限公司_库存_" + time + ".xls";

        HostVo hostVo = new HostVo("ftp.hqyy.com", 21, "gdgjyyyxgs", "gdgjyyyxgs2019@");


        CompletableFuture<Void> flowFuture = CompletableFuture.runAsync(() -> {
            try {
                List<FlowOrSaleDTO> flowList = HQDao.getFlowList("flow");
                getDataStreamAndUpload(flowFileName, flowList, FlowOrSaleDTO.class, hostVo);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
                log.error("上传沈阳红旗流向数据错误");
            }

        }, executor);

        CompletableFuture<Void> saleFuture = CompletableFuture.runAsync(() -> {
            try {
                List<FlowOrSaleDTO> saleList = HQDao.getFlowList("sale");
                getDataStreamAndUpload(sixtySaleFileName, saleList, FlowOrSaleDTO.class, hostVo);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
                log.error("上传沈阳红旗销售数据错误");
            }
        }, executor);

        CompletableFuture<Void> purFuture = CompletableFuture.runAsync(() -> {
            try {
                List<com.xh.onetwothreeupload.to.hq.PurDTO> purList = HQDao.getPurList();
                getDataStreamAndUpload(purFileName, purList, com.xh.onetwothreeupload.to.hq.PurDTO.class, hostVo);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
                log.error("上传沈阳红旗采购数据错误");
            }
        }, executor);

        CompletableFuture<Void> storeFuture = CompletableFuture.runAsync(() -> {
            try {
                List<com.xh.onetwothreeupload.to.hq.StoreDTO> storeList = HQDao.getStockList();
                getDataStreamAndUpload(storeFileName, storeList, com.xh.onetwothreeupload.to.hq.StoreDTO.class, hostVo);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
                log.error("上传沈阳红旗库存数据错误");
            }

        }, executor);

        CompletableFuture.allOf(flowFuture,purFuture,storeFuture,saleFuture).get();

    }


    /**
     * 获取文件流并上传
     *
     * @param fileName
     * @param data
     * @param clas     对应Excel头
     * @param hostVo   对应上传Ftp服务器
     * @throws FileNotFoundException
     */
    public static void getDataStreamAndUpload(String fileName, List data, Class clas, HostVo hostVo) throws FileNotFoundException, UnsupportedEncodingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ExcelWriter writer = new ExcelWriterBuilder()
                .autoCloseStream(true)
                .excelType(ExcelTypeEnum.XLS)
                .file(out)
                .head(clas)
                .build();


        // xlsx文件上上限是104W行左右,这里如果超过104W需要分Sheet
        WriteSheet writeSheet = new WriteSheet();
        writeSheet.setSheetName("target");
        writer.write(data, writeSheet);
        writer.finish();

//                write(new ByteArrayInputStream(out.toByteArray()),"C:/Users/Administrator/Desktop/unzip",fileName);
//ftp上传

        FTPTools.upload(new ByteArrayInputStream(out.toByteArray()), fileName, hostVo);
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
