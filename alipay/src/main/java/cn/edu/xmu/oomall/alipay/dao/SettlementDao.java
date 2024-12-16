package cn.edu.xmu.oomall.alipay.dao;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.alipay.mapper.AlipayDivReceiverMapper;
import cn.edu.xmu.oomall.alipay.mapper.AlipayRoyaltyDetailMapper;
import cn.edu.xmu.oomall.alipay.mapper.AlipaySettlementPoMapper;
import cn.edu.xmu.oomall.alipay.mapper.po.*;

import cn.edu.xmu.oomall.alipay.service.bo.RoyaltyDetail;
import cn.edu.xmu.oomall.alipay.service.bo.RoyaltyEntity;
import cn.edu.xmu.oomall.alipay.service.bo.Settlement;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class SettlementDao {
    private AlipaySettlementPoMapper alipaySettlementPoMapper;

    private AlipayRoyaltyDetailMapper alipayRoyaltyDetailMapper;

    private AlipayDivReceiverMapper alipayDivReceiverMapper;


    SettlementDao (AlipaySettlementPoMapper alipaySettlementPoMapper,
                   AlipayRoyaltyDetailMapper alipayRoyaltyDetailMapper,
                   AlipayDivReceiverMapper alipayDivReceiverMapper) {
        this.alipayDivReceiverMapper = alipayDivReceiverMapper;
        this.alipaySettlementPoMapper = alipaySettlementPoMapper;
        this.alipayRoyaltyDetailMapper = alipayRoyaltyDetailMapper;
    }

    public void insertRelation(String outRequestNo, String shopAccount, RoyaltyEntity royaltyEntity) {
        AlipayDivReceiver alipayDivReceiver = CloneFactory.copy(new AlipayDivReceiver(),royaltyEntity);
        alipayDivReceiver.setOutRequestNo(outRequestNo);
        alipayDivReceiver.setTransOut(shopAccount);
        alipayDivReceiverMapper.insertSelective(alipayDivReceiver);
    }

    public void deleteRelation(String outRequestNo, RoyaltyEntity royaltyEntity) {
        AlipayDivReceiverExample example = new AlipayDivReceiverExample();
        example.createCriteria().andAccountEqualTo(royaltyEntity.getAccount())
               .andOutRequestNoEqualTo(outRequestNo);
        alipayDivReceiverMapper.deleteByExample(example);
    }



    // 获得完整的结算单，没有分账明细则分账明细为null
    public Settlement retrieveSettlementByTradeNo(String tradeNo) {
        AlipaySettlementPoExample example = new AlipaySettlementPoExample();
        example.createCriteria().andTradeNoEqualTo(tradeNo);
        List<AlipaySettlementPo> alipaySettlementPos = this.alipaySettlementPoMapper.selectByExample(example);
        if(alipaySettlementPos.size() == 0){
            return null;
        }else{
            Settlement settlement = CloneFactory.copy(new Settlement(),alipaySettlementPos.get(0));

            AlipayRoyaltyDetailExample alipayRoyaltyDetailExample = new AlipayRoyaltyDetailExample();
            alipayRoyaltyDetailExample.createCriteria().andSettleNoEqualTo(settlement.getSettleNo());
            List<AlipayRoyaltyDetail> alipayRoyaltyDetails = new ArrayList<>(
                    this.alipayRoyaltyDetailMapper.selectByExample(alipayRoyaltyDetailExample));

            if(alipayRoyaltyDetails.size() > 0){
                settlement.setRoyaltyDetails(new ArrayList<>());
                for(AlipayRoyaltyDetail alipayRoyaltyDetail : alipayRoyaltyDetails){
                    RoyaltyDetail royaltyDetail = CloneFactory.copy(new RoyaltyDetail(),alipayRoyaltyDetail);
                    settlement.getRoyaltyDetails().add(royaltyDetail);
                }
            }

            return settlement;
        }
    }

    public List<RoyaltyEntity> retrieveRoyalEntityByShopAccount(String shopAccount) {
        AlipayDivReceiverExample example = new AlipayDivReceiverExample();
        example.createCriteria().andTransOutEqualTo(shopAccount);
        return getRoyaltyEntities(example);
    }

    private List<RoyaltyEntity> getRoyaltyEntities(AlipayDivReceiverExample example) {
        List<AlipayDivReceiver> alipayDivReceivers = this.alipayDivReceiverMapper.selectByExample(example);
        if(alipayDivReceivers.size() == 0){
            return new ArrayList<>();   //返回空的list,避免空指针异常
        }else{
            List<RoyaltyEntity> royaltyEntities = new ArrayList<>();
            for(AlipayDivReceiver alipayDivReceiver : alipayDivReceivers){
                RoyaltyEntity royaltyEntity = CloneFactory.copy(new RoyaltyEntity(),alipayDivReceiver);
                royaltyEntities.add(royaltyEntity);
            }
            return royaltyEntities;
        }
    }


    public List<RoyaltyEntity> retrieveRoyalEntityByOutRequestNo(String outRequestNo) {
        AlipayDivReceiverExample example = new AlipayDivReceiverExample();
        example.createCriteria().andOutRequestNoEqualTo(outRequestNo);
        return getRoyaltyEntities(example);
    }

    public void settleOrder(Settlement settlement) {
        AlipaySettlementPo alipaySettlementPo = CloneFactory.copy(new AlipaySettlementPo(),settlement);
        this.alipaySettlementPoMapper.insertSelective(alipaySettlementPo);
        if(settlement.getRoyaltyDetails()!= null && settlement.getRoyaltyDetails().size() > 0){
            for(RoyaltyDetail royaltyDetail : settlement.getRoyaltyDetails()){
                this.alipayRoyaltyDetailMapper.insertSelective(CloneFactory.copy(new AlipayRoyaltyDetail(),royaltyDetail));
            }
        }
    }


}
