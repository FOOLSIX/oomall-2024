//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.freight.mapper.openfeign;

import cn.edu.xmu.oomall.freight.mapper.openfeign.sf.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 顺丰速递接口
 * 注意：顺丰的请求和其他渠道不同，它的所有请求都是POST，且在生产模式下，请求的连接都是https://bspgw.sf-express.com/std/service
 *      它是依靠公共请求参数中的serviceCode参数进行区分的，下面每个接口注释中的第二行就是每个接口的serviceCode，
 *      在创建请求参数时需要根据所调用的接口填写这个serviceCode
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 */
@FeignClient(name = "sfexpress-service")
public interface SFExpressMapper {
    /**
     * 下订单接口-速运类API
     * EXP_RECE_CREATE_ORDER
     * https://open.sf-express.com/Api/ApiDetails?level3=393&interName=%E4%B8%8B%E8%AE%A2%E5%8D%95%E6%8E%A5%E5%8F%A3-EXP_RECE_CREATE_ORDER
     * @param param
     * @return
     */
    @PostMapping("/internal/sf")
    SFResponse<CreateOrderRet> createOrder(@RequestBody SFPostRequest<CreateOrderParam> param);

    /**
     * 订单结果查询接口-速运类API
     * EXP_RECE_SEARCH_ORDER_RESP
     * https://open.sf-express.com/Api/ApiDetails?level3=396&interName=%E8%AE%A2%E5%8D%95%E7%BB%93%E6%9E%9C%E6%9F%A5%E8%AF%A2%E6%8E%A5%E5%8F%A3-EXP_RECE_SEARCH_ORDER_RESP
     * @param param
     * @return
     */
    @PostMapping("/internal/sf")
    SFResponse<SearchOrderRet> searchOrderResp(@RequestBody SFPostRequest<SearchOrderParam> param);

    /**
     * 路由查询接口接口-速运类API
     * EXP_RECE_SEARCH_ROUTES
     * https://open.sf-express.com/Api/ApiDetails?level3=397&interName=%E8%B7%AF%E7%94%B1%E6%9F%A5%E8%AF%A2%E6%8E%A5%E5%8F%A3-EXP_RECE_SEARCH_ROUTES
     * @param param
     * @return
     */
    @PostMapping("/internal/sf")
    SFResponse<SearchRoutesRet> searchRoutes(@RequestBody SFPostRequest<SearchRoutesParam> param);

    /**
     * 订单确认/取消接口-速运类API
     * EXP_RECE_UPDATE_ORDER
     * https://open.sf-express.com/Api/ApiDetails?level3=339&interName=%E8%AE%A2%E5%8D%95%E7%A1%AE%E8%AE%A4%2F%E5%8F%96%E6%B6%88%E6%8E%A5%E5%8F%A3-EXP_RECE_UPDATE_ORDER
     * @param param
     * @return
     */
    @PostMapping("/internal/sf")
    SFResponse<UpdateOrderRet> updateOrder(@RequestBody SFPostRequest<UpdateOrderParam> param);

    /**
     * 打印面单
     * @param param
     * @return
     */
    @PostMapping("/internal/sf")
    SFResponse<PrintWaybillRet> printWaybill(@RequestBody SFPostRequest<PrintWaybillParam> param);
}
