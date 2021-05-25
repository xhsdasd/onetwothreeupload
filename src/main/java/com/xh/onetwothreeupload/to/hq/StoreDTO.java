package com.xh.onetwothreeupload.to.hq;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreDTO {
    @ExcelProperty(value ="ID")
    private String id;
    @ExcelProperty(value =" 库存商业编码")
    private String businesscode="0010001853";
    @ExcelProperty(value ="库存商业名称")
    private String businessname="广东广集医药有限公司";
    @ExcelProperty(value ="库存日期")
    private String date;
    @ExcelProperty(value ="产品编码")
    private String goodscode;
    @ExcelProperty(value ="产品名称")
    private String goodsname;
    @ExcelProperty(value ="产品规格")
    private String goodsspec;
    @ExcelProperty(value ="产品单位")
    private String unit;
    @ExcelProperty(value ="数量")
    private Integer num;
    @ExcelProperty(value ="批号")
    private String batchcode;
    @ExcelProperty(value ="单价")
    private BigDecimal price;

    @ExcelProperty(value ="金额")
    private BigDecimal amount;
    @ExcelProperty(value ="供应商编码")
    private String gyscode="0000002530";
    @ExcelProperty(value ="供应商名称")
    private String gysname="沈阳红旗制药有限公司";
    @ExcelProperty(value ="创建日期")
    private String cradates;
    @ExcelProperty(value ="上传日期")
    private String uploaddate;




}
