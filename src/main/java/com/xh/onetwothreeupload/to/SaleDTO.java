package com.xh.onetwothreeupload.to;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2020/7/11 16:27
 */
@Data
public class SaleDTO {
@ExcelProperty(value ="日期")
    private String date ;//经销商系统中记录的原始卖方代码"
@ExcelProperty(value ="销售方代码")
    private String salecode ;//经销商系统中记录的原始卖方名称"
@ExcelProperty(value ="销售方名称")
    private String salename ;//药企Code ETMS代码 "
@ExcelProperty(value ="采购方代码")
    private String purcode ;//经销商名称 "
@ExcelProperty(value ="采购方名称")
    private String purname ;//经销商系统中记录的产品代码 "
@ExcelProperty(value ="产品代码")
    private String goodcode ;//经销商系统中记录的产品名称"
@ExcelProperty(value ="产品名称")
    private String goodname ;//经销商系统中记录的产品规格"
@ExcelProperty(value ="产品规格")
    private String goodspec ;//产品批号 "
@ExcelProperty(value ="批号")
    private String batchcode ;//经销商系统中记录的产品采购入库数量"
@ExcelProperty(value ="数量")
    private String sum ;//经销商系统中记录的产品单位，如非最小单位，需提供单位转换关系"
@ExcelProperty(value ="单位")
    private String unit ;//经销商系统中记录的该笔流向的产品实际采购单价（含税） "
@ExcelProperty(value ="单价 ")
    private String price ;//经销商系统中记录的该笔流向的实际采购金额（含税） "
@ExcelProperty(value ="金额 ")
    private String amount ;//产品生产厂家名称"
    @ExcelProperty(value ="收货地址")
    private String address;//实际配送地址
    @ExcelProperty(value ="生产厂家")
    private String manufacturer  ;//官方规定产品特有编码 "
@ExcelProperty(value ="批准文号 ")
    private String approvalNo ;//数据采集时间(使用我司软件无需此字段) "
@ExcelProperty(value ="创建时间 ")
    private String createTime ;//"


}
