package cn.edu.xmu.oomall.freight.dao.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

@Repository
public class LogisticsAdaptorFactory {
    private static final Logger logger = LoggerFactory.getLogger(LogisticsAdaptor.class);

    private ApplicationContext context;

    @Autowired
    public LogisticsAdaptorFactory(ApplicationContext context) {
        this.context = context;
    }

    /**
     * 返回商铺的支付渠道服务
     * 简单工厂模式
     *
     * @return
     */
    public LogisticsAdaptor createAdaptor(Logistics logistics) {
        logger.debug("createLogisticAdaptor: logistic = {}",logistics);
        return (LogisticsAdaptor) context.getBean(logistics.getLogisticsClass());
    }
}
