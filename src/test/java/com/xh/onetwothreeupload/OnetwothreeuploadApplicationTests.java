package com.xh.onetwothreeupload;

import com.xh.onetwothreeupload.dao.baseDao;
import com.xh.onetwothreeupload.dao.testDao;
import com.xh.onetwothreeupload.to.SaleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OnetwothreeuploadApplicationTests {
@Autowired
private testDao testDao;


    @Test
    void contextLoads() {
//        uploadController
//        List<SaleDTO> saleList = saleDao.getSaleList();
        List<SaleDTO> saleList = testDao.getSaleList();
        System.out.println(saleList);
    }

}
