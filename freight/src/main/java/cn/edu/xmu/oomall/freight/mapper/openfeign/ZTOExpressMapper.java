package cn.edu.xmu.oomall.freight.mapper.openfeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import cn.edu.xmu.oomall.freight.mapper.openfeign.zt.CancelExpressOrderParam;
import cn.edu.xmu.oomall.freight.mapper.openfeign.zt.CreateExpressOrderParam;
import cn.edu.xmu.oomall.freight.mapper.openfeign.zt.GetExpressOrderParam;
import cn.edu.xmu.oomall.freight.mapper.openfeign.zt.QueryOrderTrackParam;
import cn.edu.xmu.oomall.freight.mapper.openfeign.zt.ReturnObj;

/**
 * 中通速递接口
 * @Author 李子晴
 * 2023-dgn2-001
 * 
 * 中通目前所有接口支持application/json的方式调用。所有接口目前均只支持UTF-8编码，只支持POST
 * 官方文档中，请求头固定有两个参数：x-appKey和 x-datadigest
 * x-appKey在中通开放平台-控制台-业务对接-开发者对接/服务商对接，进入应用在【应用信息】处查询（必须先在中通开放平台进行个人/企业验证，再在业务对接里创建应用，才能获得）
 * x-datadigest即数据签名，支持平台标准签名和自定义签名两种生成方式。
        1）平台标准签名：
             待签名字符串=请求body+appSecret，appSecret的获取和查看途径同x-appKey
             x-datadigest=base64(md5(待签名字符串))

        2）用户自定义签名：
             支持自助选择“是否使用时间戳（毫秒）：
             若选择无时间戳，待签名字符串=业务参数body+appSecret
             若选择有时间戳，待签名字符串=时间戳(毫秒)+业务参数body+appSecret
             x-datadigest=base64(md5(待签名字符串))，支持自助选择“md5或sha256”、“是否使用base64”进行配置使用。
 *
 * 为了便于后续开发，这里不加密，取消x-datadigest。在对象模型中，x-appKey对应Logistics-appAcount。
 */

@FeignClient(name="ztoexpress-service")
public interface ZTOExpressMapper 
{
    
/*
 * 创建订单接口
 * https://open.zto.com/#/interfaces?resourceGroup=20&apiName=zto.open.createOrder
 * @param CreateExpressOrderParam
 * @return ReturnObj
 */
@PostMapping(value = "/internal/zto.open.createOrder",consumes="application/json")
 ReturnObj createExpressOrder(@RequestHeader("x-appKey") String appKey,
                          @RequestBody CreateExpressOrderParam param);


/*
 * 查询订单接口
 * https://open.zto.com/#/interfaces?schemeCode=&resourceGroup=20&apiName=zto.open.getOrderInfo
 * @param GetExpressOrderParam
 * @return ReturnObj
 */
@PostMapping(value = "/internal/zto.open.getOrderInfo", consumes="application/json")
ReturnObj getExpressOrder(@RequestHeader("x-appKey") String appKey,
                         @RequestBody GetExpressOrderParam param);

/*
 * 取消订单接口
 * https://open.zto.com/#/interfaces?schemeCode=&resourceGroup=20&apiName=zto.open.cancelPreOrder
 *@param CancelExpressOrderParam
 *@return returnobj
 */
@PostMapping(value = "/internal/zto.open.cancelPreOrder", consumes="application/json")
ReturnObj cancelExpressOrder(@RequestHeader("x-appKey") String appKey,
                         @RequestBody CancelExpressOrderParam param);


/*
 * 查询物流轨迹接口
 * https://open.zto.com/#/interface?resourceGroup=10&apiName=zto.merchant.waybill.track.query
 *@param QueryOrderTrackParam
 *@return returnobj
 */
@PostMapping(value = "/internal/zto.merchant.waybill.track.query", consumes="application/json")
ReturnObj queryOrderTrack(@RequestHeader("x-appKey") String appKey,
                         @RequestBody QueryOrderTrackParam param);                  
}