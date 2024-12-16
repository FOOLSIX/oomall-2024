//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.controller.vo.GrouponActVo;
import cn.edu.xmu.oomall.product.controller.vo.SimpleGrouponActVo;
import cn.edu.xmu.oomall.product.controller.vo.SimpleProductVo;
import cn.edu.xmu.oomall.product.dao.bo.GrouponAct;
import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import cn.edu.xmu.oomall.product.service.GrouponActService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;


/**
 * 团购控制器
 * @author prophesier
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j
public class GrouponController {

    private final GrouponActService grouponActService;

    @GetMapping("/groupons")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject retrieveGrouponAct(@RequestParam(required = false) Long shopId,
                                           @RequestParam(required = false) Long productId,
                                           @RequestParam(required = false) LocalDateTime beginTime,
                                           @RequestParam(required = false) LocalDateTime endTime,
                                           @RequestParam(required = false, defaultValue = "1") Integer page,
                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        List<GrouponAct> actList = this.grouponActService.retrieveValidByShopIdAndProductId(shopId,productId,beginTime, endTime, page,pageSize);
        List<SimpleGrouponActVo> grouponActList = actList.stream().map(act -> {
            SimpleGrouponActVo dto;
            List<OnSale> onsaleList = act.getOnsaleList();
            if (onsaleList.size() == 0){
                log.error("retrieveGrouponAct: GrouponAct (id = {}) has no onsale");
                dto = new SimpleGrouponActVo();
            }else {
                //所有团购的销售时间都相同
                dto = CloneFactory.copy(new SimpleGrouponActVo(), onsaleList.get(0));
            }
            CloneFactory.copy(dto, act);
            return dto;
        }).collect(Collectors.toList());
        return new ReturnObject(new PageVo<>(grouponActList, page, pageSize));
    }


    @GetMapping("/groupons/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject findGrouponActById(@PathVariable Long id){
        GrouponAct act = this.grouponActService.findById(id, PLATFORM);
        return new ReturnObject(CloneFactory.copy(new GrouponActVo(),act));
    }

    @GetMapping("/groupons/{id}/products")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getGrouponActProduct(@PathVariable Long id,
                                             @RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        List<Product> products = this.grouponActService.retrieveProduct(id);
        List<SimpleProductVo> dtos = products.stream().map(obj -> new SimpleProductVo(obj)).collect(Collectors.toList());
        return new ReturnObject(new PageVo<>(dtos.subList((page-1) * pageSize, Math.min(dtos.size(), page * pageSize)), page, pageSize));
    }

}
