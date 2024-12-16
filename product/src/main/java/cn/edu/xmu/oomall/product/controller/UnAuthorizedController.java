//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.controller.vo.ProductVo;
import cn.edu.xmu.oomall.product.controller.vo.SimpleProductVo;
import cn.edu.xmu.oomall.product.dao.bo.Category;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import cn.edu.xmu.oomall.product.service.CategoryService;
import cn.edu.xmu.oomall.product.service.OnsaleService;
import cn.edu.xmu.oomall.product.service.ProductService;
import cn.edu.xmu.oomall.product.controller.vo.StateVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 商品控制器
 * @author Ming Qiu
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j
public class UnAuthorizedController {

    private final OnsaleService onsaleService;
    private final ProductService productService;
    private final CategoryService categoryService;

    /**
     * 获得商品信息
     * @author Ming Qiu
     * <p>
     * date: 2022-12-04 19:19
     * @param id
     * @return
     */
    @GetMapping("/products/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject findProductById(@PathVariable("id") Long id) {
        Product product = this.productService.findProductById(PLATFORM, id);
        return new ReturnObject(new ProductVo(product));
    }

    /**
     * 获得商品的所有状态
     * @return
     */
    @GetMapping("/products/states")
    public ReturnObject getStates(){
        return new ReturnObject(Product.STATUSNAMES.keySet().stream().map(code -> StateVo.builder().name(Product.STATUSNAMES.get(code)).code(code).build()).collect(Collectors.toList()));
    }

    /**
     * 查询正式商品
     * @return
     */
    @GetMapping("/products")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getProducts(@RequestParam Long shopId, @RequestParam String barCode, @RequestParam String name,
                                   @RequestParam(required = false,defaultValue = "1") Integer page,
                                   @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        List<Product> prodLists=this.productService.retrieveValidProducts(shopId, barCode, name, page, pageSize);
        List<SimpleProductVo> ret = prodLists.stream()
                .map(o -> new SimpleProductVo(o))
                .collect(Collectors.toList());
        return new ReturnObject(new PageVo<>(ret, page, pageSize));
    }

    /**
     * 获得商品的历史信息
     * @author Ming Qiu
     * <p>
     * date: 2022-12-04 19:19
     * @param id
     * @return
     */
    @GetMapping("/onsales/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject findOnsaleById(@PathVariable("id") Long id){
        Product product = this.productService.findByOnsaleId(id);
        ProductVo dto = new ProductVo(product);
        log.debug("findOnsaleById: dto = {}", dto);
        return new ReturnObject(dto);
    }

    /**
     * 获得二级分类
     * @param id
     * @return
     */
    @GetMapping("/categories/{id}/subcategories")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getSubCategories(@PathVariable("id") Long id) {
        List<Category> categories = this.categoryService.retrieveSubCategories(id);
        return new ReturnObject(categories.stream().map(category -> IdNameTypeVo.builder().id(category.getId()).name(category.getName()).build()).collect(Collectors.toList()));
    }

    /**
     * 查看活动中的商品
     *
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/activities/{id}/onsales")
    public ReturnObject getCouponActProduct(@PathVariable("id") Long id,
                                            @RequestParam(required = false, defaultValue = "1") Integer page,
                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        List<Product> productList = this.onsaleService.getCouponActProduct(id, page, pageSize);
        return new ReturnObject(productList.stream().map(product -> new SimpleProductVo(product)).collect(Collectors.toList()));
    }


    /**
     * 查看分类下货品
     *
     * @param id  分类id
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/subcategories/{id}/products")
    public ReturnObject getCategoryProduct(   @PathVariable Long id,
                                              @RequestParam(required = false,defaultValue = "1") Integer page,
                                              @RequestParam(required = false,defaultValue = "10") Integer pageSize) {
        List<Product> OnsaleList = this.productService.getCategoryProduct(id,page,pageSize);
        List<SimpleProductVo> collection=OnsaleList.stream().map(product -> new SimpleProductVo(product)).collect(Collectors.toList());
        PageVo<SimpleProductVo> ret = new PageVo<>(collection, page, pageSize);
        return new ReturnObject(ret);

    }




}
