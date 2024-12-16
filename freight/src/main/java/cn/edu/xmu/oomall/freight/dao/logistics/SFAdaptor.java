package cn.edu.xmu.oomall.freight.dao.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PostCreatePackageAdaptorDto;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.openfeign.SFExpressMapper;
import cn.edu.xmu.oomall.freight.mapper.openfeign.sf.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * @author CaoZhiyi
 * @Task 2023-dgn3-006
 * 顺丰物流适配器
 */

@Repository("sFAdaptor")
public class SFAdaptor implements LogisticsAdaptor{

    private static final Logger logger = LoggerFactory.getLogger(SFAdaptor.class);

    private SFExpressMapper sfExpressMapper;


    @ToString.Exclude
    @JsonIgnore
    @Setter
    private RegionDao regionDao;

    private static final String SUCCESS = "A1000";

    @Autowired
    public SFAdaptor(SFExpressMapper sfExpressMapper) {
        this.sfExpressMapper = sfExpressMapper;
    }

    private final String CREATE_PACKAGE="EXP_RECE_CREATE_ORDER";

    private final String Search_OrderResp="EXP_RECE_SEARCH_ORDER_RESP";

    private final String Search_Routes="EXP_RECE_SEARCH_ROUTES";

    private final String Update_order="EXP_RECE_UPDATE_ORDER";


    /**
     * 下订单接口-速运类API
     * EXP_RECE_CREATE_ORDER
     * https://open.sf-express.com/Api/ApiDetails?level3=393&interName=%E4%B8%8B%E8%AE%A2%E5%8D%95%E6%8E%A5%E5%8F%A3-EXP_RECE_CREATE_ORDER
     * @param contract
     * @param express
     * @return
     */

    @Override
    public PostCreatePackageAdaptorDto createPackage(Contract contract, Express express)  {

        Logistics logistics = contract.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }

        /**
         * Set param
         * */
        CreateOrderParam param = new CreateOrderParam();

        /**
         * 运单基本信息
         * */
        param.setOrderId(express.getOrderCode());

        /**
         * 托寄物信息
         * */
        CreateOrderParam.CargoDetailsDTO cargoDetail = new CreateOrderParam.CargoDetailsDTO();
        /*根据老师意见设置为默认返回值*/
        /*cargoDetail.setName(express.getGoodsName());*/

        List<CreateOrderParam.CargoDetailsDTO> cargoDetails = new ArrayList<>();
        cargoDetails.add(cargoDetail);

        param.setCargoDetails(cargoDetails);

        /**
         * 收、寄双方信息
         * */
        CreateOrderParam.ContactInfoListDTO sender=new CreateOrderParam.ContactInfoListDTO();
        sender.setMobile(express.getSendMobile());
        sender.setAddress(express.getSendAddress());
        sender.setContactType(1);

        CreateOrderParam.ContactInfoListDTO delivery=new CreateOrderParam.ContactInfoListDTO();
        delivery.setMobile(express.getReceivMobile());
        delivery.setAddress(express.getReceivAddress());
        sender.setContactType(2);

        List<CreateOrderParam.ContactInfoListDTO> contactInfoList = new ArrayList<>();
        contactInfoList.add(sender);
        contactInfoList.add(delivery);

        param.setContactInfoList(contactInfoList);

        SFPostRequest<CreateOrderParam> Param = new SFPostRequest<>();
        Param.setPartnerID(logistics.getAppAccount());
        Param.setRequestID("uuid");//需要一个全局唯一的UUID，自定义
        Param.setTimestamp(getTimeStamp());
        Param.setServiceCode(CREATE_PACKAGE);
        Param.setMsgDigest(logistics.getSecret());
        Param.setMsgData(param);

        SFResponse<CreateOrderRet> retObj= this.sfExpressMapper.createOrder(Param);
        if (!retObj.getApiResultCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", param, retObj.getApiResultCode(), retObj.getApiErrorMsg());
            /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
            return null;
        } else {
            CreateOrderRet createOrderRet = (CreateOrderRet)  retObj.getApiResultData().getMsgData();
            PostCreatePackageAdaptorDto dto = new PostCreatePackageAdaptorDto();
            /*dto中的运单id属性暂定从数据库中获取*/
            dto.setBillCode(createOrderRet.getWaybillNoInfoList().get(0).getWaybillNo());
            return dto;
        }
    }


    /**
     * 订单结果查询接口-速运类API
     * EXP_RECE_SEARCH_ORDER_RESP
     * https://open.sf-express.com/Api/ApiDetails?level3=396&interName=%E8%AE%A2%E5%8D%95%E7%BB%93%E6%9E%9C%E6%9F%A5%E8%AF%A2%E6%8E%A5%E5%8F%A3-EXP_RECE_SEARCH_ORDER_RESP
     * @param contract
     * @param billCode
     * @return
     */

    @Override
    public Express getPackage(Contract contract, String billCode)  {

        Logistics logistics = contract.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("get package:logistics is null");
        }

        /**
         * 顺丰较为特殊，查询订单时需要提供orderId即客户订单号而非运单号,这里的billcode视为客户订单号
         */
        SearchOrderParam postSearchOrderParam=new SearchOrderParam();
        postSearchOrderParam.setOrderId(billCode);

        SFPostRequest<SearchOrderParam> Param = new SFPostRequest<>();
        Param.setPartnerID(logistics.getAppAccount());
        Param.setRequestID("uuid");//需要一个全局唯一的UUID，自定义
        Param.setTimestamp(getTimeStamp());
        Param.setServiceCode(Search_OrderResp);
        Param.setMsgDigest(logistics.getSecret());
        Param.setMsgData(postSearchOrderParam);

        SFResponse<SearchOrderRet> retObj= this.sfExpressMapper.searchOrderResp(Param);
        if (!retObj.getApiResultCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", postSearchOrderParam, retObj.getApiResultCode(), retObj.getApiErrorMsg());
            /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
            return null;
        } else {
            SearchOrderRet searchOrderRet = (SearchOrderRet)  retObj.getApiResultData().getMsgData();

            Long sendRegionId=Long.parseLong(searchOrderRet.getOrigincode());
            Long receivRegionId=Long.parseLong(searchOrderRet.getDestcode());
            Region sendRegion=regionDao.findById(sendRegionId);
            Region receivRegion=regionDao.findById(receivRegionId);
            List<SearchOrderRet.WaybillNoInfoListDTO> waybillNoInfoList = searchOrderRet.getWaybillNoInfoList();

            Express express = new Express();

            /*在List中获取数据*/
            express.setBillCode(waybillNoInfoList.get(0).getWaybillNo());
            express.setSendRegionId(sendRegionId);
            express.setReceivRegionId(receivRegionId);
            express.setSendRegion(sendRegion);
            express.setReceivRegion(receivRegion);

            return express;
        }
    }

    /**
     * 订单确认/取消接口-速运类API
     * EXP_RECE_UPDATE_ORDER
     * 其余物流公司仅支持取消订单，顺丰可通过dealType支持确认订单
     * https://open.sf-express.com/Api/ApiDetails?level3=339&interName=%E8%AE%A2%E5%8D%95%E7%A1%AE%E8%AE%A4%2F%E5%8F%96%E6%B6%88%E6%8E%A5%E5%8F%A3-EXP_RECE_UPDATE_ORDER
     * @param contract
     * @param express
     * @return
     */
    @Override
    public void cancelPackage(Contract contract, Express express)  {
        Logistics logistics = contract.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("get package:logistics is null");
        }

        /**
         * 顺丰较为特殊，查询订单时需要提供orderId即客户订单号而非运单号,这里的billcode视为客户订单号
         */
        UpdateOrderParam updateOrderParam=new UpdateOrderParam();
        updateOrderParam.setOrderId(express.getOrderCode());

        /**
         * 顺丰的API较为特殊，确认收件与取消收件集成在一个API中，靠dealType来区别功能
         * 故暂时将dealType及相关方法定义在Express中
         */
        updateOrderParam.setDealType(0);

        SFPostRequest<UpdateOrderParam> param = new SFPostRequest<>();
        param.setPartnerID(logistics.getAppAccount());
        param.setRequestID("uuid");//需要一个全局唯一的UUID，自定义
        param.setTimestamp(getTimeStamp());
        param.setServiceCode(Update_order);
        param.setMsgDigest(logistics.getSecret());
        param.setMsgData(updateOrderParam);

        SFResponse<UpdateOrderRet> retObj= this.sfExpressMapper.updateOrder(param);
        if (!retObj.getApiResultCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", param, retObj.getApiResultCode(), retObj.getApiErrorMsg());
        }
    }

    /**
     * 商户发出揽收
     * @param contract
     * @param billCode
     * 顺丰暂时未使用
     */
    @Override
    public void sendPackage(Contract contract, String billCode, String orderId)  {
        Logistics logistics = contract.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("get package:logistics is null");
        }

        /**
         * 顺丰较为特殊，查询订单时需要提供orderId即客户订单号而非运单号,这里的billcode视为客户订单号
         */
        UpdateOrderParam updateOrderParam=new UpdateOrderParam();
        updateOrderParam.setOrderId(orderId);

        /**
         * 顺丰的API较为特殊，确认收件与取消收件集成在一个API中，靠dealType来区别功能
         * 故暂时将dealType及相关方法定义在Express中
         */
        updateOrderParam.setDealType(1);

        /*当DealType为1时，则为确认收货*/
        List<UpdateOrderParam.WaybillNoInfoListDTO> waybillNoInfoList = new ArrayList<>();
        UpdateOrderParam.WaybillNoInfoListDTO waybillNoInfo = new UpdateOrderParam.WaybillNoInfoListDTO();
        waybillNoInfo.setWaybillNo(billCode);
        waybillNoInfo.setWaybillType(2);
        /**
         * 这里默认是第2类即子单
         * 子单是指与母单关联的具体包裹或货物的运单号。每个子单都有独立的运输信息和目的地。
         * 当一个批次或一组货物被拆分成多个包裹进行运输时，每个包裹将被分配一个独立的子单号。*/
        waybillNoInfoList.add(waybillNoInfo);
        updateOrderParam.setWaybillNoInfoList(waybillNoInfoList);

        SFPostRequest<UpdateOrderParam> param = new SFPostRequest<>();
        param.setPartnerID(logistics.getAppAccount());
        param.setRequestID("uuid");//需要一个全局唯一的UUID，自定义
        param.setTimestamp(getTimeStamp());
        param.setServiceCode(Update_order);
        param.setMsgDigest(logistics.getSecret());
        param.setMsgData(updateOrderParam);

        SFResponse<UpdateOrderRet> retObj= this.sfExpressMapper.updateOrder(param);
        if (!retObj.getApiResultCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", param, retObj.getApiResultCode(), retObj.getApiErrorMsg());
        }

    }

    private String getTimeStamp()
    {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
    }

}
