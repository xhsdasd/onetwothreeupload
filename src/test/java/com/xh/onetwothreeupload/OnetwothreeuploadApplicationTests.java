package com.xh.onetwothreeupload;


import com.xh.onetwothreeupload.dao.HQDao;
import com.xh.onetwothreeupload.dao.OneThreeDao;
import com.xh.onetwothreeupload.scheduled.Base;
import com.xh.onetwothreeupload.to.HostVo;
import com.xh.onetwothreeupload.to.hq.StoreDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@SpringBootTest
class OnetwothreeuploadApplicationTests {
    @Autowired
    private HQDao HQDao;

    @Test
    void contextLoads() throws FileNotFoundException, UnsupportedEncodingException {
        HostVo hostVo = new HostVo("ftp.hqyy.com", 21, "gdgjyyyxgs", "gdgjyyyxgs2019@");

        String storeFileName = "广东广集医药有限公司_库存_2021-05-25.xls";
        List<StoreDTO> storeList = HQDao.getStockList();
        Base.getDataStreamAndUpload(storeFileName, storeList, com.xh.onetwothreeupload.to.hq.StoreDTO.class, hostVo );

    }

}
