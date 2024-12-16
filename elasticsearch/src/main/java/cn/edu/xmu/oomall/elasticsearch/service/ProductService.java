package cn.edu.xmu.oomall.elasticsearch.service;

import cn.edu.xmu.oomall.elasticsearch.mapper.ProductMapper;
import cn.edu.xmu.oomall.elasticsearch.mapper.po.ProductEs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    /**
     * 新增或修改产品到 Elasticsearch
     *
     * @param productEs 产品信息
     */
    public void saveOrUpdateProduct(ProductEs productEs) {
        productMapper.save(productEs);
        log.info("Product saved or updated successfully. ID: {}, Name: {}", productEs.getId(), productEs.getName());
    }

    /**
     * 根据名称、条形码、店铺ID查询产品
     *
     * @param name    产品名称
     * @param barcode 条形码（可选）
     * @param shopId  店铺ID（可选）
     * @param page    页码
     * @param size    每页大小
     * @return 产品ID列表
     */
    public List<Long> searchProducts(String name, String barcode, Long shopId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProductEs> esPage;

        if (barcode != null && shopId != null) {
            esPage = productMapper.findByNameContainingAndBarcodeAndShopId(name, barcode, shopId, pageable);
        } else {
            esPage = productMapper.findByNameContaining(name, pageable);
        }

        // 如果没有找到匹配的产品，返回空列表
        if (esPage.isEmpty()) {
            log.info("No products found with name '{}', barcode '{}', shopId '{}'.", name, barcode, shopId);
            return List.of();
        }

        List<Long> productIds = esPage.getContent().stream()
                .map(ProductEs::getId)
                .toList();

        log.info("Found products with name '{}': {}", name, productIds);

        return productIds;
    }
}
