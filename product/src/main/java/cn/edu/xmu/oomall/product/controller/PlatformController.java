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
import cn.edu.xmu.javaee.core.validation.UpdateGroup;
import cn.edu.xmu.oomall.product.controller.dto.CategoryDto;
import cn.edu.xmu.oomall.product.controller.dto.CommissionRatioDto;
import cn.edu.xmu.oomall.product.controller.vo.CategoryVo;
import cn.edu.xmu.oomall.product.dao.bo.Category;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import cn.edu.xmu.oomall.product.service.CategoryService;
import cn.edu.xmu.oomall.product.service.GrouponActService;
import cn.edu.xmu.oomall.product.service.ProductDraftService;
import cn.edu.xmu.oomall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/platforms/{shopId}", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class PlatformController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductDraftService productDraftService;
    private final GrouponActService grouponActService;

    /**
     * 获得二级分类
     * @param id
     * @return
     */
    @GetMapping("/categories/{id}/subcategories")
    @Transactional(propagation = Propagation.REQUIRED)
    @Audit(departName = "platforms")
    public ReturnObject getSubCategories(@PathVariable("id") Long id,
                                         @PathVariable("shopId") Long shopId)
    {
        if (!PLATFORM.equals(shopId))
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT, "平台管理员才可以查看分类详细");
        List<Category> categories = this.categoryService.retrieveSubCategories(id);
        List<CategoryVo> ret =  categories.stream().map(category -> new CategoryVo(category)).collect(Collectors.toList());
        return new ReturnObject(ret);
    }

    /**
     * 增加二级分类
     * @param shopId 商铺id
     * @param id 一级分类id
     * @param vo 属性
     * @param creator 操作者
     * @return
     */
    @PostMapping("/categories/{id}/subcategories")
    @Audit(departName = "platforms")
    public ReturnObject createSubCategories(@PathVariable("shopId") Long shopId,
                                             @PathVariable("id") Long id,
                                             @Validated(NewGroup.class) @RequestBody CategoryDto vo,
                                             @LoginUser UserDto creator) {
        if (!PLATFORM.equals(shopId))
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT, "平台管理员才可以修改分类");
        Category category = CloneFactory.copy(new Category(), vo);
        Category newCategory=this.categoryService.createSubCategory(id, category, creator);
        IdNameTypeVo dto= IdNameTypeVo.builder().id(newCategory.getId()).name(newCategory.getName()).build();
        return new ReturnObject(ReturnNo.CREATED,dto);
    }

    /**
     * 管理员修改分类
     * @param shopId 商铺id
     * @param id 分类id
     * @param vo 修改属性
     * @param modifier 操作者
     * @return
     */
    @PutMapping("/categories/{id}")
    @Audit(departName = "platforms")
    public ReturnObject updateCategory(@PathVariable("shopId") Long shopId,
                                       @PathVariable("id") Long id,
                                       @Validated(UpdateGroup.class) @RequestBody CategoryDto vo,
                                       @LoginUser UserDto modifier) {
        if (!PLATFORM.equals(shopId))
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT, "平台管理员才可以修改分类");
        Category category = CloneFactory.copy(new Category(), vo);
        category.setId(id);
        this.categoryService.updateCategory(category, modifier);
        return new ReturnObject();
    }

    @DeleteMapping("/categories/{id}")
    @Audit(departName = "platforms")
    public ReturnObject deleteCategory(@PathVariable("shopId") Long shopId,
                                       @PathVariable("id") Long id,
                                       @LoginUser UserDto userDto) {
        if (!shopId.equals(PLATFORM))
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT, "平台管理员才可以修改分类");
        this.categoryService.deleteCategory(id, userDto);
        return new ReturnObject();
    }

    /**
     * 管理员修改分账比例
     * @param shopId 店铺id
     * @param id 商品id
     * @param user 操作者
     * @return
     */
    @PutMapping("/products/{id}/commissionratio")
    @Audit(departName = "platforms")
    public ReturnObject putProductCommissionRatio(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user,
                                                  @RequestBody @Validated CommissionRatioDto dto){
        if (!shopId.equals(PLATFORM)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "商品", id, shopId));
        }
        Product product=new Product();
        product.setCommissionRatio(dto.getCommissionRatio());
        this.productService.updateProduct(shopId,id,user,product);
        return new ReturnObject();
    }

    /**
     * 管理员解禁商品
     * @param shopId
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/products/{id}/allow")
    @Audit(departName = "platforms")
    public ReturnObject allowGoods(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user){
        if (!shopId.equals(PLATFORM)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "商品", id, shopId));
        }
        this.productService.allowProduct(id, user);
        return new ReturnObject();
    }

    /**
     * 平台管理员禁售商品
     * @param shopId
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/products/{id}/prohibit")
    @Audit(departName = "platforms")
    public ReturnObject prohibitGoods(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user){
        if (!shopId.equals(PLATFORM)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "商品", id, shopId));
        }
        this.productService.prohibitProduct(id, user);
        return new ReturnObject();
    }

    /**
     * 货品发布
     * @param shopId
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/draftproducts/{id}/publish")
    @Audit(departName = "platforms")
    public ReturnObject publishProduct(@PathVariable Long shopId, @PathVariable Long id, @RequestBody CommissionRatioDto vo, @LoginUser UserDto user){

        if(PLATFORM != shopId){
            //只有平台管理员能发布商品
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "上架商品", id, shopId));
        }
        if(vo.getCommissionRatio()>100 || vo.getCommissionRatio()<0){
            throw new BusinessException(ReturnNo.FIELD_NOTVALID,String.format(ReturnNo.FIELD_NOTVALID.getMessage(),"CommissionRatio"));
        }
        Integer commissionRatio = vo.getCommissionRatio();
        Product product = this.productDraftService.publishProduct(shopId, id, commissionRatio, user);
        IdNameTypeVo dto = IdNameTypeVo.builder().id(product.getId()).name(product.getName()).build();
        return new ReturnObject(dto);
    }

    /**
     * 平台管理员审核团购活动
     */
    @Audit(departName = "platforms")
    @PutMapping("/groupons/{id}/publish")
    public ReturnObject publishGrouponAct(@PathVariable Long shopId, @PathVariable Long id, UserDto userDto){
        if (PLATFORM != shopId){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, "平台管理员才能审核");
        }
        this.grouponActService.publishGroupon(id, userDto);
        return new ReturnObject();
    }
}
