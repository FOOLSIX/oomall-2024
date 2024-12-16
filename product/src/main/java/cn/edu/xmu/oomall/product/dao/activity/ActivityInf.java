//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.dao.activity;

import cn.edu.xmu.oomall.product.dao.bo.Activity;
import cn.edu.xmu.oomall.product.mapper.po.ActivityPo;

import java.util.List;

public interface ActivityInf {

    /**
     * 从mongo中获取剩下的一半
     * @author Ming Qiu
     * <p>
     * date: 2022-11-27 18:21
     * @param po
     * @return
     */
    Activity getActivity(ActivityPo po) throws RuntimeException;

    String insert(Activity bo) throws RuntimeException;

    void save(Activity bo) throws RuntimeException;
}
