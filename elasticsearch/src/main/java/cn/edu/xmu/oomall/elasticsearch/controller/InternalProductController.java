package cn.edu.xmu.oomall.elasticsearch.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.elasticsearch.mapper.po.ProductEs;
import cn.edu.xmu.oomall.elasticsearch.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/internal/products", produces = "application/json;charset=UTF-8")
public class InternalProductController {

    private final ProductService productService;

    /**
     * 新增或修改产品到 Elasticsearch
     *
     * @param productEs 产品信息
     * @return 操作结果
     */
    @PostMapping
    public ReturnObject saveOrUpdateProduct(@RequestBody ProductEs productEs) {
        productService.saveOrUpdateProduct(productEs);
        return new ReturnObject(ReturnNo.OK, "ProductEs saved or updated successfully");
    }

    /**
     * 根据名称、条形码、店铺ID查询产品
     *
     * @param name 产品名称（必填）
     * @param barcode 条形码（可选）
     * @param shopId 店铺ID（可选）
     * @param page 页码（默认1）
     * @param size 每页大小（默认10）
     * @return 查询结果
     */
    @GetMapping
    public ReturnObject searchProducts(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "barcode", required = false) String barcode,
            @RequestParam(value = "shopId", required = false) Long shopId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Long> productIds = productService.searchProducts(name, barcode, shopId, page, size);
        return new ReturnObject(ReturnNo.OK, productIds);
    }

}
