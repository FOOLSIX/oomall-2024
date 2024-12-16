package cn.edu.xmu.oomall.elasticsearch.mapper;

import cn.edu.xmu.oomall.elasticsearch.mapper.po.ProductEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductMapper extends ElasticsearchRepository<ProductEs, Long> {

    // 按照 name 模糊查询
    Page<ProductEs> findByNameContaining(String name, Pageable pageable);

    // 按照 name、barcode 和 shopId 查询
    Page<ProductEs> findByNameContainingAndBarcodeAndShopId(String name, String barcode, Long shopId, Pageable pageable);
}

