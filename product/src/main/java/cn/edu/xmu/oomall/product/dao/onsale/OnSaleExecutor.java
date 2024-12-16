//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.dao.onsale;

import cn.edu.xmu.oomall.product.dao.bo.OnSale;

/**
 * OnsaleDao的command模式
 * 用于返回不同的onsale
 */
public interface OnSaleExecutor {
    public OnSale execute();
}
