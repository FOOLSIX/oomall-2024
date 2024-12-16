package cn.edu.xmu.oomall.product.controller;


import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.javaee.core.validation.UpdateGroup;
import cn.edu.xmu.oomall.product.controller.dto.GrouponActDto;
import cn.edu.xmu.oomall.product.controller.dto.OnSaleDto;
import cn.edu.xmu.oomall.product.dao.bo.GrouponAct;
import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import cn.edu.xmu.oomall.product.service.GrouponActService;
import cn.edu.xmu.oomall.product.controller.vo.FullGrouponActVo;
import cn.edu.xmu.oomall.product.controller.vo.SimpleGrouponActVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 团购控制器
 * @author prophesier
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j
public class ShopGrouponController {

    private final GrouponActService grouponActService;

    /**
     * 商户查看特定团购详情
     * @param shopId
     * @param actId
     * @return
     */
    @GetMapping("/groupons/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    @Audit(departName = "shops")
    public ReturnObject findGrouponById(@PathVariable Long shopId, @PathVariable("id") Long actId){
        GrouponAct act = this.grouponActService.findByShopIdAndActId(shopId, actId);
        return new ReturnObject(new FullGrouponActVo(act));
    }

    // 商户查询商铺的所有状态团购
    @GetMapping("/groupons")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject retrieveByShopId(@PathVariable Long shopId,
                                         @RequestParam(required = false) Long productId,
                                         @RequestParam(required = false, defaultValue = "1") Integer status,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize){

        List<GrouponAct> ret = this.grouponActService.retrieveByShopId(shopId, productId, status, page, pageSize);
        log.debug("retrieveByShopId: ret.size = {}",ret.size());

        List<SimpleGrouponActVo> grouponActList = ret.stream()
                .map(act -> {
                    List<OnSale> onsaleList = act.getOnsaleList();
                    if (!onsaleList.equals(null) && act.getOnsaleList().size() >= 1) {
                        // 同一个活动中所有时间相同，只需要用第一个进行拷贝时间
                        OnSale onsale = onsaleList.get(0);
                        SimpleGrouponActVo dto = CloneFactory.copy(new SimpleGrouponActVo(), onsale);
                        CloneFactory.copy(dto, act);
                        log.debug("retrieveByShopId: dto = {}",dto);
                        return dto;
                    }else{
                        log.error("retrieveByShopId: only one onsale obj is allowed for groupon act (id = {})",act.getId());
                        return null;
                    }
                }).collect(Collectors.toList());
        return new ReturnObject(new PageVo(grouponActList, page, pageSize));
    }



    /**
     *  商户新增团购活动
     */
    @PostMapping("/products/{pid}/groupons")
    @Audit(departName = "shops")
    public ReturnObject createGrouponAct(@PathVariable Long shopId,
                                         @PathVariable Long pid,
                                         @Validated(NewGroup.class) @RequestBody GrouponActDto vo,
                                         @LoginUser UserDto user){
        GrouponAct act = CloneFactory.copy(new GrouponAct(), vo);
        act.setShopId(shopId);
        act.setActClass(GrouponAct.ACTCLASS);
        act.setThresholds(vo.getThresholds());
        OnSale onsale =  CloneFactory.copy(new OnSale(), vo);
        onsale.setProductId(pid);
        onsale.setShopId(shopId);
        onsale.setType(OnSale.GROUPON);
        onsale.setDeposit(0L);
        GrouponAct grouponAct =  this.grouponActService.createGrouponAct(act, onsale, user);
        return new ReturnObject(ReturnNo.CREATED, IdNameTypeVo.builder().id(grouponAct.getId()).name(grouponAct.getName()).build());
    }


    /**
     * 商户将产品加入到团购活动中
     * @param shopId
     * @param pid
     * @param id
     * @param vo
     * @param user
     * @return
     */
    @Audit(departName = "shops")
    @PostMapping("/products/{pid}/groupons/{id}")
    public ReturnObject createGrouponAct(@PathVariable Long shopId,
                                         @PathVariable Long pid,
                                         @PathVariable Long id,
                                         @Validated(UpdateGroup.class) @RequestBody OnSaleDto vo,
                                         @LoginUser UserDto user){
        OnSale onsale = CloneFactory.copy(new OnSale(), vo);
        onsale.setProductId(pid);
        onsale.setShopId(shopId);
        GrouponAct grouponAct =  this.grouponActService.addToGrouponAct(id, onsale, user);
        return new ReturnObject(ReturnNo.CREATED, IdNameTypeVo.builder().id(grouponAct.getId()).name(grouponAct.getName()).build());
    }

    /**
     * 将产品上的团购活动取消
     * @param shopId
     * @param pid
     * @param id
     * @param user
     * @return
     */
    @Audit(departName = "shops")
    @DeleteMapping("/products/{pid}/groupons/{id}")
    public ReturnObject cancelProductGrouponAct(@PathVariable Long shopId,
                                                @PathVariable Long pid,
                                                @PathVariable Long id,
                                                @LoginUser UserDto user) {
        this.grouponActService.cancelFromGrouponAct(shopId, id, pid, user);
        return new ReturnObject();
    }

    /**
     * 商户修改团购活动
     */
    @Audit(departName = "shops")
    @PutMapping("/groupons/{id}")
    public ReturnObject updateGrouponAct(@PathVariable Long shopId,
                                         @PathVariable Long id,
                                         @RequestBody GrouponActDto vo,
                                         @LoginUser UserDto user){
        GrouponAct act = CloneFactory.copy(new GrouponAct(), vo);
        act.setShopId(shopId);
        act.setId(id);
        OnSale onsale = CloneFactory.copy(new OnSale(), vo);
        onsale.setId(null);
        onsale.setShopId(shopId);
        this.grouponActService.updateGroupon(act, onsale, user);
        return new ReturnObject();
    }

    /**
     * 商户取消团购活动
     * @param shopId
     * @param id
     * @return
     */
    @Audit(departName = "shops")
    @DeleteMapping("/groupons/{id}")
    public ReturnObject deleteGrouponAct(@PathVariable Long shopId, @PathVariable Long id, UserDto userDto){
        this.grouponActService.cancel(shopId, id, userDto);
        return new ReturnObject();
    }

}
