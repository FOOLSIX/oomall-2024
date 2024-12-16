package cn.edu.xmu.oomall.freight.dao.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PostCreatePackageAdaptorDto;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.openfeign.ZTOExpressMapper;
import cn.edu.xmu.oomall.freight.mapper.openfeign.zt.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 * 中通适配器
 * 传入适配器的对象都应该是满血对象
 */
@Repository("zTOAdaptor")
public class ZTOAdaptor implements LogisticsAdaptor {
    private static final Logger logger = LoggerFactory.getLogger(ZTOAdaptor.class);

    private ZTOExpressMapper ztoExpressMapper;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private RegionDao regionDao;

    private static final String SUCCESS = "SYS000";

    private static final Map<String,Byte> stringToStatusCode=new HashMap<>();
    static
    {
        stringToStatusCode.put("收件",Express.UNSHIPPED);
        stringToStatusCode.put("派件",Express.SHIPPED);
        stringToStatusCode.put("签收",Express.SIGNED);
        stringToStatusCode.put("问题件拒收",Express.REJECTED);
        stringToStatusCode.put("返回发件网点",Express.RETURNED);
        stringToStatusCode.put("返回发件人地址",Express.RETURNED);
        //todo 对于问题件，中通只返回名为”问题件“的scanType，无法据此确定该问题件是”丢失“还是”破损“，此处暂定”问题件“对应的是”丢失状态
        stringToStatusCode.put("问题件",Express.LOST);
    }

    @Autowired
    public ZTOAdaptor(ZTOExpressMapper ztoExpressMapper) {
        this.ztoExpressMapper = ztoExpressMapper;
    }

    /**
     * 创建运单
     * https://open.zto.com/#/interface?resourceGroup=20&apiName=zto.open.createOrder
     * @param contract
     * @param express
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Override
    public PostCreatePackageAdaptorDto createPackage(Contract contract, Express express)  {

        Logistics logistics = contract.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }

        /*Set param*/
        CreateExpressOrderParam param = new CreateExpressOrderParam();

        /**
         * 运单基本信息
         */
        AccountInfo accountInfo=new AccountInfo();
        accountInfo.setAccountType(contract.getAccount());
        accountInfo.setAccountPassword(contract.getSecret());
        param.setAccountInfo(accountInfo);
        param.setOrderType(express.getOrderType());
        param.setPartnerOrderCode(express.getOrderCode());
        /**
         * 寄件人信息
         */
        SenderInfo senderInfo = new SenderInfo();
        //暂定senderId是Express对象中的属性
        senderInfo.setSenderId(express.getSendId().toString());
        senderInfo.setSenderName(express.getSendName());
        senderInfo.setSenderMobile(express.getSendMobile());

        //todo 暂定Express中的RegionId的行政区划级别为”区“
        List<Region> senderParentRegions = express.getSendRegion().getAncestors();
        //设置省
        senderInfo.setSenderProvince(senderParentRegions.get(1).getName());
        //设置市
        senderInfo.setSenderCity(senderParentRegions.get(0).getName());
        //设置区
        senderInfo.setSenderDistrict(express.getSendRegion().getName());
        senderInfo.setSenderAddress(express.getSendAddress());

        param.setSenderObject(senderInfo);

        /**
         * 收件人信息
         */
        ReceiveInfo receiveInfo = new ReceiveInfo();
        receiveInfo.setReceiverName(express.getReceivName());
        receiveInfo.setReceiverMobile(express.getReceivMobile());

        //todo 暂定Express中的RegionId的行政区划级别为”区“
        List<Region> receivParentRegion = express.getReceivRegion().getAncestors();
        //设置省
        receiveInfo.setReceiverProvince(receivParentRegion.get(1).getName());
        //设置市
        receiveInfo.setReceiverCity(receivParentRegion.get(0).getName());
        //设置区
        receiveInfo.setReceiverDistrict(express.getReceivRegion().getName());
        receiveInfo.setReceiverAddress(express.getReceivAddress());

        param.setReceiverObject(receiveInfo);

        ReturnObj retObj = this.ztoExpressMapper.createExpressOrder(logistics.getAppId(), param);
        if (!retObj.getStatusCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", param, retObj.getStatusCode(), retObj.getMessage());
            /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
            return null;
        } else {
            CreateExpressOrderRetObj createExpressOrderRetObj = (CreateExpressOrderRetObj) retObj.getResult();

            PostCreatePackageAdaptorDto dto = new PostCreatePackageAdaptorDto();
            /*dto中的运单id属性暂定从数据库中获取*/
            dto.setBillCode(createExpressOrderRetObj.getBillCode());
            return dto;
        }
    }

    /**
     * 查询运单
     * https://open.zto.com/#/interface?schemeCode=&resourceGroup=20&apiName=zto.open.getOrderInfo
     * @param contract
     * @param billCode
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Override
    public Express getPackage(Contract contract, String billCode)  {

        Logistics logistics = contract.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("get package:logistics is null");
        }

        /*Set Param*/
        GetExpressOrderParam param = new GetExpressOrderParam();
        param.setBillCode(billCode);

        ReturnObj retObj = this.ztoExpressMapper.getExpressOrder(logistics.getAppId(), param);
        if (!retObj.getStatusCode().equals(SUCCESS)) {
            logger.error("getPackage：param = {}, code = {}, message = {}", param, retObj.getStatusCode(), retObj.getMessage());
            /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
            return null;
        }
        else
        {
            GetExpressOrderRetObj getExpressOrderRetObj = (GetExpressOrderRetObj) retObj.getResult();

            Long sendRegionId=regionDao.retrieveByName(getExpressOrderRetObj.getSendCounty()).getId();
            Long receivRegionId=regionDao.retrieveByName(getExpressOrderRetObj.getReceivCounty()).getId();
            Region sendRegion=regionDao.findById(sendRegionId);
            Region receivRegion=regionDao.findById(receivRegionId);

            Express express = new Express();

            //设置运单的地区信息
            express.setSendRegionId(sendRegionId);
            express.setReceivRegionId(receivRegionId);
            express.setSendRegion(sendRegion);
            express.setReceivRegion(receivRegion);

            //设置运单的其他信息
            express.setOrderType(getExpressOrderRetObj.getOrderType());
            express.setReceivName(getExpressOrderRetObj.getReceivName());
            express.setReceivAddress(getExpressOrderRetObj.getReceivAddress());
            express.setSendMobile(getExpressOrderRetObj.getSendMobile());
            express.setReceivMobile(getExpressOrderRetObj.getReceivMobile());
            express.setSendName(getExpressOrderRetObj.getSendName());
            express.setBillCode(getExpressOrderRetObj.getBillCode());
            express.setSendAddress(getExpressOrderRetObj.getSendAddress());
            express.setOrderCode(getExpressOrderRetObj.getOrderCode());
            /*暂定取消原因和取消类型是Express中的属性*/
            express.setCancelReason(getExpressOrderRetObj.getCancelReason());
            express.setSecStatus(getExpressOrderRetObj.getSecStatus());

            //获取运单状态
            QueryOrderTrackParam queryOrderTrackParam=new QueryOrderTrackParam();
            queryOrderTrackParam.setBillCode(billCode);
            ReturnObj statusReturnObj=this.ztoExpressMapper.queryOrderTrack(logistics.getAppId(), queryOrderTrackParam);
            if(!statusReturnObj.getStatusCode().equals(SUCCESS))
            {
                logger.error("queryOrderTrack：param = {}, code = {}, message = {}", queryOrderTrackParam, statusReturnObj.getStatusCode(), statusReturnObj.getMessage());
                /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
                return null;
            }
            else
            {
                QueryOrderTrackRetObj queryOrderTrackRetObj=(QueryOrderTrackRetObj) statusReturnObj.getResult();

                String scanTypeOrReturnType= StringUtils.hasText(queryOrderTrackRetObj.getReturnType())?
                        queryOrderTrackRetObj.getReturnType():
                        queryOrderTrackRetObj.getScanType();

                Optional<Byte> updateStatusOpt=Optional.ofNullable(stringToStatusCode.get(scanTypeOrReturnType));
                if(updateStatusOpt.isPresent())
                {
                    express.setStatus(updateStatusOpt.get());
                }
                else
                {
                    //todo 异常未定义
                    throw new IllegalArgumentException();
                }
                return express;
            }
        }
    }

    /**
     * 取消订单
     * https://open.zto.com/#/interface?schemeCode=&resourceGroup=20&apiName=zto.open.cancelPreOrder
     * @param contract
     * @param express
     * @throws NoSuchAlgorithmException
     */
    @Override
    public void cancelPackage(Contract contract, Express express)  {

        Logistics logistics = contract.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }

        /*Set Param*/
        CancelExpressOrderParam param = new CancelExpressOrderParam();
        /*暂定secStatus是Express对象中的属性*/
        param.setCancelType(express.getSecStatus());
        param.setBillCode(express.getBillCode());

        ReturnObj retObj = this.ztoExpressMapper.cancelExpressOrder(logistics.getAppId(), param);
        if (!retObj.getStatusCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", param, retObj.getStatusCode(), retObj.getMessage());
            /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
        }
    }

    /**
     * 商户发出揽收
     * @param contract
     * @param billCode
     */
    @Override
    public void sendPackage(Contract contract, String billCode, String orderId)  {

        Logistics logistics = contract.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }

        Express express=getPackage(contract, billCode);

        /*Set param*/
        CreateExpressOrderParam param = new CreateExpressOrderParam();

        /**
         * 运单基本信息
         */
        SummaryInfo summaryInfo=new SummaryInfo();
        summaryInfo.setStartTime(new Date());

        param.setOrderType(express.getOrderType());
        param.setPartnerOrderCode(express.getOrderCode());
        param.setSummaryInfo(summaryInfo);

        /**
         * 寄件人信息
         */
        SenderInfo senderInfo = new SenderInfo();
        //暂定senderId是Express对象中的属性
        senderInfo.setSenderId(express.getSendId().toString());
        senderInfo.setSenderName(express.getSendName());
        senderInfo.setSenderMobile(express.getSendMobile());

        //todo 暂定Express中的RegionId的行政区划级别为”区“
        List<Region> senderParentRegions = express.getSendRegion().getAncestors();
        //设置省
        senderInfo.setSenderProvince(senderParentRegions.get(1).getName());
        //设置市
        senderInfo.setSenderCity(senderParentRegions.get(0).getName());
        //设置区
        senderInfo.setSenderDistrict(express.getSendRegion().getName());
        senderInfo.setSenderAddress(express.getSendAddress());

        param.setSenderObject(senderInfo);

        /**
         * 收件人信息
         */
        ReceiveInfo receiveInfo = new ReceiveInfo();
        receiveInfo.setReceiverName(express.getReceivName());
        receiveInfo.setReceiverMobile(express.getReceivMobile());

        //todo 暂定Express中的RegionId的行政区划级别为”区“
        List<Region> receivParentRegion = express.getReceivRegion().getAncestors();
        //设置省
        receiveInfo.setReceiverProvince(receivParentRegion.get(1).getName());
        //设置市
        receiveInfo.setReceiverCity(receivParentRegion.get(0).getName());
        //设置区
        receiveInfo.setReceiverDistrict(express.getReceivRegion().getName());
        receiveInfo.setReceiverAddress(express.getReceivAddress());

        param.setReceiverObject(receiveInfo);

        ReturnObj retObj = this.ztoExpressMapper.createExpressOrder(logistics.getAppId(), param);
        if (!retObj.getStatusCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", param, retObj.getStatusCode(), retObj.getMessage());
            /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
        }
    }

}