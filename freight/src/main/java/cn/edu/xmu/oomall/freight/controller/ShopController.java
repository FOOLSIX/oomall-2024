package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.dto.ContractDto;
import cn.edu.xmu.oomall.freight.controller.vo.ContractVo;
import cn.edu.xmu.oomall.freight.controller.dto.ModifyShopLogisticsDto;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.service.ContractService;
import cn.edu.xmu.oomall.freight.service.LogisticsService;
import cn.edu.xmu.oomall.freight.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final ContractService contractService;
    private final WarehouseService warehouseService;
    private final LogisticsService logisticsService;


    /**
     * 查询仓库物流
     * @param shopId
     * @param id
     * @param page  (not required)
     * @param pageSize (not required)
     * @return
     */
    @GetMapping("/warehouses/{id}/contracts")
    @Audit(departName = "shops")
    public ReturnObject getWarehouseLogistics(@PathVariable Long shopId,
                                              @PathVariable Long id,
                                              @RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                              @LoginUser UserDto user){
        return new ReturnObject();
    }

    /**
     * 商家新增物流合作信息
     *
     */
    @PostMapping("/warehouses/{id}/logistics/{lid}/contracts")
    @Audit(departName = "shops")
    public ReturnObject addShopLogistics(@PathVariable Long shopId,
                                         @RequestBody ContractDto contractDto,
                                         @LoginUser UserDto user){
        Contract contract = CloneFactory.copy(new Contract(), contractDto);
        contract.setShopId(shopId);
        contract = contractService.addContract(contract, user);

        return new ReturnObject(ReturnNo.CREATED, CloneFactory.copy(new ContractVo(), contract));
    }


    /**
     * 商家修改物流模板
     *
     */
    @PutMapping("/contracts/{id}")
    @Audit(departName = "shops")
    public ReturnObject changeLogisticsContract(@PathVariable Long shopId,
                                                @PathVariable Long id,
                                                @RequestBody ModifyShopLogisticsDto modifyShopLogisticsDto,
                                                @LoginUser UserDto user){
        Contract contract = CloneFactory.copy(new Contract(), modifyShopLogisticsDto);
        contract.setShopId(shopId);
        contract.setId(id);
        this.contractService.modifyContract(contract, user);
        return new ReturnObject();
    }

    /**
     * 商铺停用物流合作
     *
     */
    @PutMapping("/contracts/{id}/suspend")
    @Audit(departName = "shops")
    public ReturnObject suspendShopLogistics(@PathVariable Long shopId,
                                             @PathVariable Long id,
                                             @LoginUser UserDto user){
        Contract contract = new Contract();
        contract.setShopId(shopId);
        contract.setId(id);
        contract.setInvalid(Contract.INVALID);
        contractService.modifyContract(contract, user);
        return new ReturnObject();
    }

    /**
     * 商铺启用物流合作
     *
     */
    @PutMapping("/contracts/{id}/resume")
    @Audit(departName = "shops")
    public ReturnObject resumeShopLogistics(@PathVariable Long shopId,
                                             @PathVariable Long id,
                                             @LoginUser UserDto user){

        Contract contract = new Contract();
        contract.setShopId(shopId);
        contract.setId(id);
        contract.setInvalid(Contract.VALID);
        contractService.modifyContract(contract, user);
        return new ReturnObject();
    }



}
