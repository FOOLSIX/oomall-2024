//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.freight.mapper.openfeign;

import cn.edu.xmu.oomall.freight.mapper.openfeign.jt.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 极兔速递接口
 * 统一的参数解释：
 * apiAccount：接入方在平台的api账户标识，对应Logistics中的appAcount
 * digest：base64(md5(业务参数的Json+privateKey)),privateKey对应Logistics中的secret
 * apiAccount和privateKey由平台提供
 * 简化需求，digest只需要填写privateKey，无需加密
 * timestamp：时间戳
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */
@FeignClient(name = "jtexpress-service")
public interface JTExpressMapper {
    /**
     * 创建订单接口
     * https://open.jtexpress.com.cn/#/apiDoc/orderserve/create
     * 数据类型：X-WWW-FORM-URLENCODED
     *
     * @param bizContent bizContent的内容为AddExpressOrderParam的json序列化数据
     * @return AddExpressOrderRetObj
     */
    @PostMapping(value = "/internal/webopenplatformapi/apiorder/addOrder", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ReturnObj<AddExpressOrderRetObj> addExpressOrder(@RequestHeader("apiAccount") String apiAccount,
                                                     @RequestHeader("timestamp") long timestamp,
                                                     @RequestBody String bizContent);


    /**
     * 创建订单（带运单号）接口
     * https://open.jtexpress.com.cn/#/apiDoc/orderserve/createWithWaybillNo
     * 数据类型：X-WWW-FORM-URLENCODED
     *
     * @param bizContent bizContent的内容为addExpressOrderWithBillCodeParam的json序列化数据
     * @return ReturnObj<AddExpressOrderWithBillCodeRetObj>
     */
    @PostMapping(value = "/internal/webopenplatformapi/apiorder/v2/addOrder", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ReturnObj<AddExpressOrderWithBillCodeRetObj> addExpressOrderWithBillCode(@RequestHeader("apiAccount") String apiAccount,
                                                                             @RequestHeader("timestamp") long timestamp,
                                                                             @RequestBody String bizContent);


    /**
     * 取消订单接口
     * https://open.jtexpress.com.cn/#/apiDoc/orderserve/cancel
     * 数据类型：X-WWW-FORM-URLENCODED
     *
     * @param bizContent bizContent为cancelOrderParam的json序列化数据
     * @return ReturnObj<CancelExpressOrderRetObj>
     */
    @PostMapping(value = "/internal/webopenplatformapi/apiorder/cancelOrder", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ReturnObj<CancelExpressOrderRetObj> cancelOrder(@RequestHeader("apiAccount") String apiAccount,
                                                    @RequestHeader("timestamp") long timestamp,
                                                    @RequestBody String bizContent);

    /**
     * 物流轨迹查询接口
     * https://open.jtexpress.com.cn/#/apiDoc/logistics/query
     * 数据类型：X-WWW-FORM-URLENCODED
     *
     * @param bizContent bizContent为getLogisticsTraceParam的json序列化数据
     * @return ReturnObj<List<GetLogisticsTraceRetObj>>
     * 返回的是一个list，每一个list的成员都是GetLogisticsTraceRetObj对象，GetLogisticsTraceRetObj里面包含billCode以及物流轨迹list
     */
    @PostMapping(value = "/internal/webopenplatformapi/apilogistics/trace", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ReturnObj<List<GetLogisticsTraceRetObj>> getLogisticsTrace(@RequestHeader("apiAccount") String apiAccount,
                                                               @RequestHeader("timestamp") long timestamp,
                                                               @RequestBody String bizContent);


}
