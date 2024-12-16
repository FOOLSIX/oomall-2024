//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.freight.dao.logistics;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PostCreatePackageAdaptorDto;
import cn.edu.xmu.oomall.freight.mapper.openfeign.JTExpressMapper;
import cn.edu.xmu.oomall.freight.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.freight.mapper.openfeign.jt.*;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author 丁圳杰
 * @Task 2023-dgn3-001
 * 极兔适配器
 */
@Repository("jTAdaptor")
@RequiredArgsConstructor
public class JTAdaptor implements LogisticsAdaptor {
    //feignClient
    private final JTExpressMapper jtExpressMapper;
    private final RegionMapper regionMapper;
    private final ExpressDao expressDao ;//由于jtMapper不返回对应express基本信息,于是加入ExpressDao查询信息
    private static final Map<String,Byte> stringToStatusCode=new HashMap<>();
    static {
        stringToStatusCode.put("快件揽收",Express.SHIPPED);
        stringToStatusCode.put("快件签收",Express.SIGNED);
        stringToStatusCode.put("客户拒收",Express.REJECTED);
        stringToStatusCode.put("包裹异常-网点",Express.LOST);
        stringToStatusCode.put("包裹异常",Express.LOST);
        stringToStatusCode.put("包裹异常-中心",Express.LOST);
        stringToStatusCode.put("退回件-网点",Express.RETURNED);
        stringToStatusCode.put("退回件-中心",Express.RETURNED);
        stringToStatusCode.put("物流轨迹长时间不改变",Express.LOST);
    }
    private PersonInfo fillSenderInfo(Express express, PersonInfo senderInfo) {
        senderInfo.setName(express.getSendName());
        senderInfo.setMobile(express.getSendMobile());
        senderInfo.setPhone(express.getSendMobile());
        fillRegion(express, senderInfo);
        senderInfo.setAddress(express.getSendAddress());//详细地址
        return senderInfo;
    }
    private PersonInfo fillReceiverInfo(Express express, PersonInfo receiverInfo) {
        receiverInfo.setName(express.getSendName());
        receiverInfo.setMobile(express.getSendMobile());
        receiverInfo.setPhone(express.getSendMobile());
        fillRegion(express, receiverInfo);
        receiverInfo.setAddress(express.getReceivAddress());//详细地址
        return receiverInfo;
    }
    private void fillRegion(Express express, PersonInfo sendeInfo) {
        //todo 暂定Express中的RegionId的行政区划级别为”区“
        List<RegionPo> regionPos=null;
        RegionPo areaRegion=null;
        try {
            InternalReturnObject<List<RegionPo>> internalReturnObject = regionMapper.retrieveParentRegionsById(express.getSendRegionId());
            regionPos = internalReturnObject.getData();
            InternalReturnObject<RegionPo> areaRegionRet = regionMapper.findRegionById(express.getSendRegionId());
            areaRegion = areaRegionRet.getData();
        } catch (Exception e) {
            throw new RuntimeException("JTAdapter调用regionMapper方法异常");
        }
        if (regionPos.isEmpty()) {
            throw new RuntimeException("regionPos为empty");
        }
        //根据retrieveParentsRegions源码获取得到的list是倒序

        sendeInfo.setArea(areaRegion.getName());//区县
        //sendeInfo.setCountryCode();//国,默认中国,所以不设
        sendeInfo.setCity(regionPos.get(0).getName());//市
        sendeInfo.setProv(regionPos.get(1).getName());//省
    }

    @Override
    public PostCreatePackageAdaptorDto createPackage(Contract contract, Express express) {

        Logistics logistics = contract.getLogistics();
        if (Objects.isNull(logistics)) {
            throw new IllegalArgumentException("create package:logistics is null");
        }
        AddExpressOrderParam param = new AddExpressOrderParam();
//        param.setCustomerCode(String.valueOf(shopLogistics.getAccount()));
//        param.setDigest(shopLogistics.getSecret());
        param.setTxlogisticId(express.getOrderCode());
        //默认值
//        param.setExpressType();
//        param.setOrderType();
//        param.setServiceType();
//        param.setDeliveryType();
//        param.setPayType();
//param.setServiceType();
        PersonInfo senderInfo = new PersonInfo();
        fillSenderInfo(express, senderInfo);
        express.setContract(contract);
        
        PersonInfo receiverInfo = new PersonInfo();
        fillReceiverInfo(express,receiverInfo);
        param.setCustomerCode(contract.getAccount());
        param.setSendStartTime(localDateTimeConvertToString(express.getStartTime()));
        param.setSendEndTime(localDateTimeConvertToString(express.getEndTime()));
        param.setSender(senderInfo);
        param.setReceiver(receiverInfo);
        //源代码报错，已修改
        param.setGoodsType(express.getGoodsType());
        param.setWeight(String.valueOf(express.getWeight()));
        if(express.getStartTime()!=null&&express.getEndTime()!=null){
            param.setSendStartTime(localDateTimeConvertToString(express.getStartTime()));
            param.setSendEndTime(localDateTimeConvertToString(express.getEndTime()));
        }else if(express.getEndTime()!=null){
            //处理只有揽收结束的时间,类似加急件,必须在截止时间前发货
            param.setSendStartTime(localDateTimeConvertToString(LocalDateTime.now()));
            param.setSendEndTime(localDateTimeConvertToString(express.getEndTime()));
        }
        else{
            //todo:之后可能会调整
            //如果没有时间的话,则默认从现在起到24小时后
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime nextDay = currentDateTime.plusDays(1);
            param.setSendStartTime(localDateTimeConvertToString(currentDateTime));
            param.setSendEndTime(localDateTimeConvertToString(nextDay));
            //todo:由于未定,所以暂不将对应时间更新进数据库
        }
        String jsonStr = JacksonUtil.toJson(param);
        AddExpressOrderRetObj data =null;
        try {
            ReturnObj<AddExpressOrderRetObj> addExpressOrderRetObjReturnObj = jtExpressMapper.addExpressOrder(logistics.getAppId(), System.currentTimeMillis(), jsonStr);
            data = addExpressOrderRetObjReturnObj.getData();
            String billCode = data.getBillCode();
            PostCreatePackageAdaptorDto postCreatePackageAdaptorDto = new PostCreatePackageAdaptorDto();
            postCreatePackageAdaptorDto.setId(express.getId());
            postCreatePackageAdaptorDto.setBillCode(billCode);
            return postCreatePackageAdaptorDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("createExpressOrder调用jtExpressMapper.addExpressOrder异常");
        }
    }
    @Override
    public Express getPackage(Contract contract, String billCode) {
        Logistics logistics = contract.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }
        GetLogisticsTraceParam logisticsTraceParam = new GetLogisticsTraceParam();
        //预设billCode为唯一订单的情况下
        logisticsTraceParam.setBillCodes(billCode);
        ReturnObj<List<GetLogisticsTraceRetObj>> returnObj = jtExpressMapper.getLogisticsTrace(logistics.getAppId(), System.currentTimeMillis(), JacksonUtil.toJson(logisticsTraceParam));
        List<GetLogisticsTraceRetObj> data = returnObj.getData();
        GetLogisticsTraceRetObj traceRetObj = data.get(0);
        //在对方list按时间排序的情况下
        ArrayList<TraceDetail> details = traceRetObj.getDetails();
        TraceDetail traceDetail = details.get(details.size() - 1);
        //来一个映射器
        String scanTypeOrProblemType= StringUtils.hasText(traceDetail.getScanType())?traceDetail.getScanType():traceDetail.getProblemType();
        Express express = expressDao.findByBillCode(contract.getShopId(), billCode);
        //todo:由于目前express没有对应的traceDetail的字段,于是只封装了scanType
        Optional<Byte> updateStatusOpt = Optional.ofNullable(stringToStatusCode.get(scanTypeOrProblemType));
        if (updateStatusOpt.isPresent()) {
            if(express.allowStatus(updateStatusOpt.get())){
                express.setStatus(updateStatusOpt.get());
            }else{
                //todo:异常未定义
                throw new RuntimeException();
            }
        }else{
            //todo:异常未定义
            throw new RuntimeException();
        }

        return express;
    }

    @Override
    public void cancelPackage(Contract contract, Express express){
        Logistics logistics = contract.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }
        CancelExpressOrderParam cancelParam = new CancelExpressOrderParam();
        cancelParam.setTxlogisticId(express.getOrderCode());
        cancelParam.setReason(express.getCancelReason());
        String param = JacksonUtil.toJson(cancelParam);
        //todo 填入Logistics中的appAcount
        ReturnObj<CancelExpressOrderRetObj> returnObj = jtExpressMapper.cancelOrder(logistics.getAppId(),System.currentTimeMillis(), param);
        String code = returnObj.getCode();
        if ("1".equals(code)) {

        } else {
            throw new RuntimeException("错误response");
        }
        return;
    }

    @Override
    public void sendPackage(Contract contract, String billCode, String orderId) {
        Express express = expressDao.findByBillCode(contract.getShopId(), billCode);
        //在create方法默认传null
        createPackage(contract,express);
    }


    public String localDateTimeConvertToString(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }

}
