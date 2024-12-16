package cn.edu.xmu.oomall.product.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.ProductEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "elasticsearch-service", path = "/internal/products")
public interface SearchMapper {

    /**
     * 新增或修改产品到 Elasticsearch
     */
    @PostMapping
    ReturnObject saveOrUpdateProduct(@RequestBody ProductEs productEs);

    /**
     * 按名称模糊查询产品
     */
    @GetMapping
    InternalReturnObject<List<Long>> searchProductsByName(
            @RequestParam("name") String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    );

    /**
     * 按名称、条形码和店铺ID组合查询产品
     */
    @GetMapping
    InternalReturnObject<List<Long>> searchProductsByNameAndBarcodeAndShopId(
            @RequestParam("name") String name,
            @RequestParam("barcode") String barcode,
            @RequestParam("shopId") Long shopId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    );
}
