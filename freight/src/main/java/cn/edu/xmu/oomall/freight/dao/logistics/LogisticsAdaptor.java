//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.freight.dao.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Express;

import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PostCreatePackageAdaptorDto;
import java.security.NoSuchAlgorithmException;
/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 * 物流平台接口
 */


/**
 * @author 曹志逸
 * @Task 2023-dgn3-006
 * 物流平台接口
 */
/**
 * 物流渠道适配器接口
 * 适配器模式
 */
public interface LogisticsAdaptor {

    /**
     * 创建运单
     * @param contract
     * @param express
     * @return
     * @throws NoSuchAlgorithmException
     */
    PostCreatePackageAdaptorDto createPackage(Contract contract, Express express) ;

    /**
     * 查询运单
     * @param contract
     * @param billCode
     * @return
     * @throws NoSuchAlgorithmException
     */
    Express getPackage(Contract contract, String billCode) ;

    /**
     * 取消运单
     * @param contract
     * @param express
     * @throws NoSuchAlgorithmException
     */
    void cancelPackage(Contract contract, Express express) ;

    /**
     * 商户发出揽收
     * @param contract
     * @param billCode
     */
    void sendPackage(Contract contract, String billCode, String orderId) ;

}