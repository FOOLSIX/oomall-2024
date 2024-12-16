//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller;


import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.oomall.product.controller.vo.*;
import cn.edu.xmu.oomall.product.controller.dto.*;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.oomall.product.dao.bo.*;
import cn.edu.xmu.oomall.product.service.OnsaleService;
import cn.edu.xmu.oomall.product.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 商品控制器
 * @author Ming Qiu
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j
public class ShopProductController {

    private final OnsaleService onsaleService;
    private final ProductService productService;
    private final TransactionTemplate transactionTemplate;

    @GetMapping("/onsales/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    @Audit(departName = "shops")
    public ReturnObject getOnsaleById(@PathVariable Long shopId, @PathVariable Long id) {
        OnSale onsale = this.onsaleService.findById(shopId, id);
        return new ReturnObject(CloneFactory.copy(new OnsaleVo(), onsale));
    }

    @PostMapping("/products/{id}/onsales")
    @Audit(departName = "shops")
    public ReturnObject addOnsale(@PathVariable Long shopId,
                                  @PathVariable("id") Long id,
                                  @Validated(NewGroup.class) @RequestBody OnSaleDto vo,
                                  @LoginUser UserDto user){

        if (vo.getBeginTime().isAfter(vo.getEndTime())){
            throw new BusinessException(ReturnNo.LATE_BEGINTIME);
        }

        if (null == vo.getPayTime()){
            vo.setPayTime(vo.getBeginTime());
        }

        if (vo.getPayTime().isBefore(vo.getBeginTime())){
            throw new BusinessException(ReturnNo.ADV_SALE_TIMEEARLY);
        }

        if (vo.getPayTime().isAfter(vo.getEndTime())){
            throw new BusinessException(ReturnNo.ADV_SALE_TIMELATE);
        }

        OnSale onsale = CloneFactory.copy(new OnSale(), vo);

        SimpleOnsaleVo dto = this.transactionTemplate.execute(status -> {
            OnSale newOnsale = this.onsaleService.insert(shopId, id, onsale, user);
            SimpleOnsaleVo simpleOnsaleVo = CloneFactory.copy(new SimpleOnsaleVo(), newOnsale);
            return simpleOnsaleVo;
        });

        return new ReturnObject(ReturnNo.CREATED,dto);
    }

    @GetMapping("/products/{id}/onsales")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getAllOnsale(
            @PathVariable(value = "shopId",required = true) Long shopId,
            @PathVariable(value = "id",required = true) Long id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        List<OnSale> onsales = this.onsaleService.retrieveByProductId(shopId, id, page, pageSize);
        List<SimpleOnsaleVo> ret = onsales.stream().map(obj -> CloneFactory.copy(new SimpleOnsaleVo(), obj)).collect(Collectors.toList());
        return new ReturnObject(new PageVo<>(ret, page, pageSize));
    }

    @DeleteMapping("/onsales/{id}")
    @Audit(departName = "shops")
    public ReturnObject delOnsaleId(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @LoginUser UserDto user
    )
    {
        this.onsaleService.tryToDel(shopId, id, user);
        return new ReturnObject();
    }

    @PutMapping("/onsales/{id}/cancel")
    @Audit(departName = "shops")
    public ReturnObject cancelOnsaleId(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @LoginUser UserDto user
    )
    {
        this.onsaleService.cancel(shopId,id,user);
        return new ReturnObject();
    }

    /**
     * 店家查看货品信息详情
     * @param shopId 店铺id
     * @param id 商品id
     * @return
     */
    @GetMapping("products/{id}")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getProductId(@PathVariable Long shopId, @PathVariable Long id){
        Product product = this.productService.findProductById(shopId, id);
        return new ReturnObject(new FullProductVo(product));
    }

    /**
     * 店家修改无需审核的货品信息
     * @param shopId 店铺id
     * @param id 商品id
     * @param user 操作者
     * @return
     */
    @PutMapping("products/{id}")
    @Audit(departName = "shops")
    public ReturnObject putProductId(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user,
                                     @RequestBody @Validated ProductDto dto){


        Product product = CloneFactory.copy(new Product(), dto);
        this.productService.updateProduct(shopId, id, user, product);

        return new ReturnObject();
    }



    /**
     * 查看运费模板用到的商品
     * @param shopId
     * @param id
     * @return
     */
    @GetMapping("/templates/{id}/products")
    @Audit(departName = "shops")
    public ReturnObject getTemplateProduct(@PathVariable Long shopId, @PathVariable Long id,
                                           @RequestParam(required = false,defaultValue = "1") Integer page,
                                           @RequestParam(required = false,defaultValue = "10") Integer pageSize){
         List<Product> products = this.productService.getTemplateProduct(shopId, id, page, pageSize);
        List<IdNameTypeVo> collect = products.stream().map(o -> IdNameTypeVo.builder().id(o.getId()).name(o.getName()).build()).collect(Collectors.toList());
        PageVo<IdNameTypeVo> idNameDtoPageDto = new PageVo<>(collect, page, pageSize);
        return new ReturnObject(idNameDtoPageDto);
    }

    /**
     * 查看使用特殊物流货品
     *
     * @param shopId
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/shoplogistics/{id}/products")
    @Audit(departName = "shops")
    public ReturnObject getLogisticsProduct(@PathVariable Long shopId, @PathVariable Long id,
                                            @RequestParam(required = false,defaultValue = "1") Integer page,
                                            @RequestParam(required = false,defaultValue = "10") Integer pageSize) {
        List<Product> products = this.productService.getLogisticsProduct(shopId, id, page, pageSize);
        List<IdNameTypeVo> collect = products.stream().map(o -> IdNameTypeVo.builder().id(o.getId()).name(o.getName()).build()).collect(Collectors.toList());
        PageVo<IdNameTypeVo> idNameDtoPageDto = new PageVo<>(collect, page, pageSize);
        return new ReturnObject(idNameDtoPageDto);
    }






    /**
     * 将两个商品设为相关
     * @param shopId
     * @param id
     * @param user
     * @return
     */
    @PostMapping("/products/{id}/relations")
    @Audit(departName = "shops")
    public ReturnObject relateProductId(@PathVariable Long shopId, @PathVariable Long id, @RequestBody @Validated RelateProductDto relateProductDto,
                                        @LoginUser UserDto user){
        this.productService.relateProductId(shopId, id, relateProductDto.getProductId(), user);
        return new ReturnObject();
    }

    /**
     * 将商品取消相关
     * @param shopId
     * @param id
     * @param user
     * @return
     */
    @DeleteMapping("/products/{id}/relations")
    @Audit(departName = "shops")
    public ReturnObject delRelateProduct(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user){
        this.productService.delRelateProduct(shopId, id, user);
        return new ReturnObject();
    }
}
