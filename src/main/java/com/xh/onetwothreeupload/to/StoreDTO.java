package com.xh.onetwothreeupload.to;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StoreDTO {
    @ExcelProperty(value ="日期")
    private String date ;//经销商系统中记录的产品库存日期，即时库存 "
@ExcelProperty(value ="公司代码")
    private String businessCode ;//药企Code ETMS代码 "
@ExcelProperty(value ="公司名称")
    private String businessName ;//经销商系统中的经销商自身名称"
@ExcelProperty(value ="产品代码")
    private String goodscode ;//经销商系统中记录的产品代码 "
@ExcelProperty(value ="产品名称")
    private String goodsname ;//经销商系统中记录的产品名称"
@ExcelProperty(value ="产品规格")
    private String goodsspec ;//经销商系统中记录的产品规格"
@ExcelProperty(value ="批号")
    private String batchcode ;//产品批号 "
@ExcelProperty(value ="数量 ")
    private String storeNum ;//库存数量，可销售数据+不可销数量 "
@ExcelProperty(value ="单位")
    private String unit ;//经销商系统中记录的产品单位，如非最小单位，需提供单位转换关系"
@ExcelProperty(value ="生产厂家")
    private String manufacturer ;//产品生产厂家名称"
@ExcelProperty(value ="库存状态 ")
    private String storeFlag ;//正常可销，待检，破损等 "
@ExcelProperty(value ="批准文号 ")
    private String approvalNo ;//官方规定产品特有编码 "
@ExcelProperty(value ="创建时间 ")
    private String createTime ;//数据采集时间(使用我司软件无需此字段 "

}
