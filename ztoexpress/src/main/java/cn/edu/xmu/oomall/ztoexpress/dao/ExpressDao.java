package cn.edu.xmu.oomall.ztoexpress.dao;

import cn.edu.xmu.oomall.ztoexpress.controller.vo.QueryTrackVo;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.ExpressBo;
import cn.edu.xmu.oomall.ztoexpress.mapper.ExpressPoMapper;
import cn.edu.xmu.oomall.ztoexpress.mapper.SitePoMapper;
import cn.edu.xmu.oomall.ztoexpress.unit.ZTOException;
import cn.edu.xmu.oomall.ztoexpress.unit.ZTOReturnResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static cn.edu.xmu.oomall.ztoexpress.unit.ZTOReturnNo.*;

@Repository
@RefreshScope
@Transactional
public class ExpressDao {
    private ExpressPoMapper expressPoMapper;
    private SitePoMapper sitePoMapper;
    @Autowired
    public ExpressDao(ExpressPoMapper expressPoMapper,SitePoMapper sitePoMapper){
        this.expressPoMapper=expressPoMapper;
        this.sitePoMapper=sitePoMapper;
    }
    public ZTOReturnResult queryTrack(ExpressBo expressBo){
        Logger logger = LoggerFactory.getLogger(ExpressBo.class);
        if(expressBo.getExpressPo().getBillcode()==null){
            logger.info("运单号为空");
            throw new ZTOException(S208);
        }
        expressBo.setExpressPo(expressPoMapper.findByBillcode(expressBo.getExpressPo().getBillcode()));
        if(expressBo.getExpressPo()==null){
            logger.info("运单不存在");
            throw new ZTOException(E404);
        }
        expressBo.setSitePo(sitePoMapper.findByIdIs(expressBo.getExpressPo().getScansiteid()));
        if(expressBo.getSitePo()==null){
            logger.info("运单存在，但是网点不存在");
            throw new ZTOException(E404);
        }
        logger.info("返回轨迹");
        return new ZTOReturnResult(SYS000,new QueryTrackVo(expressBo.getExpressPo().getScandate(),expressBo.getSitePo()));

    }
}
