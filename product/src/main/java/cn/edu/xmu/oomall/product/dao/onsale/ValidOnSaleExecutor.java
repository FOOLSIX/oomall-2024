//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.dao.onsale;

import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获得当前有效的onsale
 */
@Slf4j
public class ValidOnSaleExecutor implements OnSaleExecutor {


    private OnSaleDao onsaleDao;

    private Long productId;

    public ValidOnSaleExecutor(OnSaleDao onsaleDao, Long productId) {
        this.onsaleDao = onsaleDao;
        this.productId = productId;
    }

    @Override
    public OnSale execute() {
        log.debug("execute: productId = {}", this.productId);
        return this.onsaleDao.findLatestValidOnsaleByProductId(this.productId);
    }
}
