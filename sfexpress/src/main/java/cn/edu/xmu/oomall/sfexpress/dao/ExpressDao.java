package cn.edu.xmu.oomall.sfexpress.dao;

import cn.edu.xmu.oomall.sfexpress.dao.bo.CargoDetail;
import cn.edu.xmu.oomall.sfexpress.dao.bo.ContactInfo;
import cn.edu.xmu.oomall.sfexpress.dao.bo.Express;
import cn.edu.xmu.oomall.sfexpress.exception.SFErrorCodeEnum;
import cn.edu.xmu.oomall.sfexpress.exception.SFException;
import cn.edu.xmu.oomall.sfexpress.mapper.SfexpressCargoDetailPoMapper;
import cn.edu.xmu.oomall.sfexpress.mapper.SfexpressExpressPoMapper;
import cn.edu.xmu.oomall.sfexpress.mapper.po.SfexpressCargoDetailPo;
import cn.edu.xmu.oomall.sfexpress.mapper.po.SfexpressExpressPo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@Repository
@RefreshScope
@Transactional
public class ExpressDao {

    private final SfexpressExpressPoMapper sfexpressExpressPoMapper;
    private final SfexpressCargoDetailPoMapper sfexpressCargoDetailPoMapper;

    @Autowired
    public ExpressDao(SfexpressExpressPoMapper sfexpressExpressPoMapper, SfexpressCargoDetailPoMapper sfexpressCargoDetailPoMapper) {
        this.sfexpressExpressPoMapper = sfexpressExpressPoMapper;
        this.sfexpressCargoDetailPoMapper = sfexpressCargoDetailPoMapper;
    }

    /**
     * 保存物流订单
     *
     * @param express
     */
    public void saveExpress(Express express) {
        List<CargoDetail> cargoDetails = express.getCargoDetails();
        List<ContactInfo> contactInfoList = express.getContactInfoList();

        SfexpressExpressPo expressPo = new SfexpressExpressPo();
        BeanUtils.copyProperties(express, expressPo);

        for (ContactInfo contactInfo : contactInfoList) {
            String address = contactInfo.getAddress();
            String city = contactInfo.getCity();
            String contact = contactInfo.getContact();
            Integer contactType = contactInfo.getContactType();
            String country = contactInfo.getCountry();
            String county = contactInfo.getCounty();
            String mobile = contactInfo.getMobile();

            if (contactType == 1) {
                expressPo.setSenderAddress(address);
                expressPo.setSenderCity(city);
                expressPo.setSenderContact(contact);
                expressPo.setSenderMobile(mobile);
            }
            if (contactInfo.getContactType() == 2) {
                expressPo.setDiliverAddress(address);
                expressPo.setDiliverCity(city);
                expressPo.setDiliverContact(contact);
                expressPo.setDiliverMobile(mobile);
            }
        }
        sfexpressExpressPoMapper.save(expressPo);

        Optional<SfexpressExpressPo> expressPoSaved = sfexpressExpressPoMapper.findByOrderId(express.getOrderId());
        if (expressPoSaved.isEmpty()) {
            throw new SFException(SFErrorCodeEnum.E8016);
        }

        List<SfexpressCargoDetailPo> cargoDetailPoList = new ArrayList<>();
        for (CargoDetail cargoDetail : cargoDetails) {
            String name = cargoDetail.getName();
            SfexpressCargoDetailPo cargoDetailPo = new SfexpressCargoDetailPo();
            cargoDetailPo.setName(name);
            cargoDetailPo.setExpressId(expressPoSaved.get().getId());
            cargoDetailPoList.add(cargoDetailPo);
        }
        sfexpressCargoDetailPoMapper.saveAll(cargoDetailPoList);
    }

    /**
     * 创建物流订单
     *
     * @param express
     * @throws SFException
     */
    public void createExpress(Express express) throws SFException {
        String orderId = express.getOrderId();
        Optional<SfexpressExpressPo> expressPoOptional = sfexpressExpressPoMapper.findByOrderId(orderId);
        if (expressPoOptional.isPresent()) {
            throw new SFException(SFErrorCodeEnum.E8016);
        }
        this.saveExpress(express);
    }

    /**
     * 更新物流订单
     * @param express
     */
    public void updateExpress(Express express) {
        String orderId = express.getOrderId();
        Optional<SfexpressExpressPo> expressPoOptional = sfexpressExpressPoMapper.findByOrderId(orderId);
        // 如果订单不存在
        if (expressPoOptional.isEmpty()) {
            throw new SFException(SFErrorCodeEnum.E6150);
        }
        SfexpressExpressPo expressPo = expressPoOptional.get();
        int status = expressPo.getStatus();
        if (status == ExpressStatusEnum.CANCELED.getCode()) {
            // 如果订单状态为已取消
            throw new SFException(SFErrorCodeEnum.E8037);
        } else if (status == ExpressStatusEnum.COMPLETED.getCode()) {
            // 如果订单状态为已完成
            throw new SFException(SFErrorCodeEnum.E8252);
        }
        BeanUtils.copyProperties(express, expressPo);
        List<ContactInfo> contactInfoList = express.getContactInfoList();
        for (ContactInfo contactInfo : contactInfoList) {
            if (contactInfo.getContactType() == 1) {
                expressPo.setSenderAddress(contactInfo.getAddress());
                expressPo.setSenderCity(contactInfo.getCity());
                expressPo.setSenderContact(contactInfo.getContact());
                expressPo.setSenderMobile(contactInfo.getMobile());
            }
            if (contactInfo.getContactType() == 2) {
                expressPo.setDiliverAddress(contactInfo.getAddress());
                expressPo.setDiliverCity(contactInfo.getCity());
                expressPo.setDiliverContact(contactInfo.getContact());
                expressPo.setSenderMobile(contactInfo.getMobile());
                expressPo.setDiliverMobile(contactInfo.getMobile());
            }
        }
        expressPo.setStatus(ExpressStatusEnum.COMPLETED.getCode());
        sfexpressExpressPoMapper.updateExpressDetails(expressPo);
    }

    /**
     * 取消订单
     * @param orderId
     */
    public void cancelExpress(String orderId) {
        Optional<SfexpressExpressPo> expressPoOptional = sfexpressExpressPoMapper.findByOrderId(orderId);
        if (expressPoOptional.isEmpty()) {
            throw new SFException(SFErrorCodeEnum.E6150);
        }
        SfexpressExpressPo expressPo = expressPoOptional.get();
        int status = expressPo.getStatus();
        if (status == ExpressStatusEnum.CANCELED.getCode()) {
            // 如果订单状态为已取消
            throw new SFException(SFErrorCodeEnum.E8037);
        } else if (status == ExpressStatusEnum.COMPLETED.getCode()) {
            // 如果订单状态为已完成
            throw new SFException(SFErrorCodeEnum.E8252);
        }
        expressPo.setStatus(ExpressStatusEnum.CANCELED.getCode());
        sfexpressExpressPoMapper.updateExpressDetails(expressPo);
    }

    /**
     * 根据orderId查询物流单
     *
     * @param orderId
     * @return
     * @throws SFException
     */
    public Express getExpressByOrderId(String orderId) throws SFException {
        Optional<SfexpressExpressPo> expressPoOptional = sfexpressExpressPoMapper.findByOrderId(orderId);
        return getExpressFromPo(expressPoOptional);
    }

    /**
     * 根据运单号查询物流订单
     *
     * @param waybillNo
     * @return
     * @throws SFException
     */
    public Express getExpressByWaybillNo(String waybillNo) throws SFException {
        Optional<SfexpressExpressPo> expressPoOptional = sfexpressExpressPoMapper.findByWaybillNo(waybillNo);
        return getExpressFromPo(expressPoOptional);
    }

    private Express getExpressFromPo(Optional<SfexpressExpressPo> expressPoOptional) throws SFException {
        if (expressPoOptional.isEmpty()) {
            return null;
        }
        SfexpressExpressPo expressPo = expressPoOptional.get();
        Express express = new Express();
        BeanUtils.copyProperties(expressPo, express);

        // 构造联系人列表
        String senderAddress = expressPo.getSenderAddress();
        String senderCity = expressPo.getSenderCity();
        String senderContact = expressPo.getSenderContact();
        String senderMobile = expressPo.getSenderMobile();
        String diliverAddress = expressPo.getDiliverAddress();
        String diliverCity = expressPo.getDiliverCity();
        String diliverContact = expressPo.getDiliverContact();
        String diliverMobile = expressPo.getDiliverMobile();
        List<ContactInfo> contactInfos = new ArrayList<>();
        ContactInfo sender = new ContactInfo();
        sender.setContact(senderContact);
        sender.setAddress(senderAddress);
        sender.setCity(senderCity);
        sender.setContactType(1);
        sender.setMobile(senderMobile);
        ContactInfo diliver = new ContactInfo();
        diliver.setAddress(diliverAddress);
        diliver.setCity(diliverCity);
        diliver.setContact(diliverContact);
        diliver.setContactType(2);
        diliver.setMobile(diliverMobile);
        contactInfos.add(sender);
        contactInfos.add(diliver);
        express.setContactInfoList(contactInfos);

        Optional<List<SfexpressCargoDetailPo>> cargoDetailPoListOptional = sfexpressCargoDetailPoMapper.findByExpressId(expressPo.getId());
        if (cargoDetailPoListOptional.isPresent()) {
            List<SfexpressCargoDetailPo> cargoDetailPoList = cargoDetailPoListOptional.get();
            List<CargoDetail> cargoDetails = new ArrayList<>();
            for (SfexpressCargoDetailPo cargoDetailPo : cargoDetailPoList) {
                CargoDetail cargoDetail = new CargoDetail();
                cargoDetail.setName(cargoDetailPo.getName());
                cargoDetails.add(cargoDetail);
            }
            express.setCargoDetails(cargoDetails);
        }

        return express;
    }
}
