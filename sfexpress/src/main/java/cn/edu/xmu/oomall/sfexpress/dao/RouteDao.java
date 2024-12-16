package cn.edu.xmu.oomall.sfexpress.dao;

import cn.edu.xmu.oomall.sfexpress.dao.bo.Express;
import cn.edu.xmu.oomall.sfexpress.dao.bo.Route;
import cn.edu.xmu.oomall.sfexpress.dao.bo.RouteResps;
import cn.edu.xmu.oomall.sfexpress.exception.SFErrorCodeEnum;
import cn.edu.xmu.oomall.sfexpress.exception.SFException;
import cn.edu.xmu.oomall.sfexpress.mapper.SfexpressRoutePoMapper;
import cn.edu.xmu.oomall.sfexpress.mapper.po.SfexpressRoutePo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@Repository
@RefreshScope
public class RouteDao {
    private final SfexpressRoutePoMapper sfexpressRoutePoMapper;

    private final ExpressDao expressDao;

    @Autowired
    public RouteDao(SfexpressRoutePoMapper sfexpressRoutePoMapper, ExpressDao expressDao) {
        this.sfexpressRoutePoMapper = sfexpressRoutePoMapper;
        this.expressDao = expressDao;
    }

    /**
     * 根据运单查询路由
     * @param mailNo
     * @return
     */

    public RouteResps getRouteRespsByMailNo(String mailNo) {
        Optional<List<SfexpressRoutePo>> routePoListOptional = sfexpressRoutePoMapper.findByMailNo(mailNo);
        RouteResps routeResps = new RouteResps();
        routeResps.setMailNo(mailNo);
        if (routePoListOptional.isEmpty()) {
            throw new SFException(SFErrorCodeEnum.E8024);
        }
        if (routePoListOptional.get().isEmpty()) {
            throw new SFException(SFErrorCodeEnum.E8024);
        }
        List<SfexpressRoutePo> routePoList = routePoListOptional.get();
        List<Route> routeList = new ArrayList<>();
        for (SfexpressRoutePo routePo : routePoList) {
            Route route = new Route();
            BeanUtils.copyProperties(routePo, route);
            routeList.add(route);
        }
        routeResps.setRoutes(routeList);
        return routeResps;
    }

    public void saveRouteResps(RouteResps routeResps) {
        String mailNo = routeResps.getMailNo();
        List<Route> routes = routeResps.getRoutes();
        List<SfexpressRoutePo> routePoList = new ArrayList<>();
        for (Route route : routes) {
            SfexpressRoutePo routePo = new SfexpressRoutePo();
            BeanUtils.copyProperties(route, routePo);
            routePo.setMailNo(mailNo);
            routePoList.add(routePo);
        }
        sfexpressRoutePoMapper.saveAll(routePoList);
    }
}
