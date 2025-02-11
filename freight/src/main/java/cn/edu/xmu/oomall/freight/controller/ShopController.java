package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.javaee.core.validation.UpdateGroup;
import cn.edu.xmu.oomall.freight.controller.dto.ContractDto;
import cn.edu.xmu.oomall.freight.controller.dto.WarehouseDto;
import cn.edu.xmu.oomall.freight.controller.dto.WarehouseRegionDto;
import cn.edu.xmu.oomall.freight.controller.vo.ContractVo;
import cn.edu.xmu.oomall.freight.controller.vo.RegionVo;
import cn.edu.xmu.oomall.freight.controller.vo.WarehouseRegionVo;
import cn.edu.xmu.oomall.freight.controller.vo.WarehouseVo;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import cn.edu.xmu.oomall.freight.service.ContractService;
import cn.edu.xmu.oomall.freight.service.LogisticsService;
import cn.edu.xmu.oomall.freight.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 获得仓库物流
     * @param shopId
     * @param page  (not required)
     * @param pageSize (not required)
     * @return
     */
    @GetMapping("warehouses/{id}/contracts")
    @Audit(departName = "shops")
    public ReturnObject getLogisticsContracts(@PathVariable Long shopId,
                                              @PathVariable Long id,
                                              @RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                              @LoginUser UserDto user){
        List<Contract> contracts = this.contractService.getContractsByWareHouseId(shopId,id,page,pageSize);
        List<ContractVo> contractVos = contracts.stream()
                .map(bo -> CloneFactory.copy(new ContractVo(),bo))
                .collect(java.util.stream.Collectors.toList());
        return new ReturnObject(new PageVo<>(contractVos, page, pageSize));
    }

    /**
     * 商家新建仓库物流日结合同
     *
     */
    @PostMapping("/warehouses/{id}/logistics/{lid}/contracts")
    @Audit(departName = "shops")
    public ReturnObject addLogisticsContract(@PathVariable Long shopId,
                                             @PathVariable Long id,
                                             @PathVariable Long lid,
                                             @RequestBody @Validated({NewGroup.class}) ContractDto contractDto,
                                             @LoginUser UserDto user){
        Contract contract = CloneFactory.copy(new Contract(), contractDto);
        contract.setShopId(shopId);
        contract.setLogisticsId(lid);
        contract.setWarehouseId(id);
        contract = this.contractService.addContract(contract, user);
        return new ReturnObject(ReturnNo.CREATED,CloneFactory.copy(new ContractVo(),contract));
    }


    /**
     * 商家修改仓库物流合同
     *
     */
    @PutMapping("/contracts/{id}")
    @Audit(departName = "shops")
    public ReturnObject changeLogisticsContract(@PathVariable Long shopId,
                                                @PathVariable Long id,
                                                @RequestBody @Validated({UpdateGroup.class}) ContractDto contractDto,
                                                @LoginUser UserDto user){
        Contract contract = CloneFactory.copy(new Contract(), contractDto);
        this.contractService.modifyContract(shopId,id,user,contract);
        return new ReturnObject();
    }

    /**
     * 商铺删除仓库物流合同
     *
     */
    @DeleteMapping ("/contracts/{id}")
    @Audit(departName = "shops")
    public ReturnObject delLogistics(@PathVariable Long shopId,
                                     @PathVariable Long id,
                                     @LoginUser UserDto user){
        this.contractService.deleteContract(shopId,id,user);
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
        contractService.resumeAndSuspendContract(shopId,id,user,Contract.VALID);
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
        contractService.resumeAndSuspendContract(shopId,id,user,Contract.INVALID);
        return new ReturnObject();
    }
    /**
     * @author 37220222203558
     * 2024-dsg116-商家获得仓库
     */
    @GetMapping("/warehouses")
    @Audit(departName = "shops")
    public ReturnObject getWarehouses(@PathVariable Long shopId,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                      @LoginUser UserDto user){
        List<Warehouse> warehouseList = warehouseService.getWarehouses(shopId, page, pageSize);
        List<WarehouseVo> voList = warehouseList.stream()
                .map(warehouse -> CloneFactory.copy(new WarehouseVo(), warehouse))
                .collect(java.util.stream.Collectors.toList());
        return new ReturnObject(voList);
    }

    /**
     * @author 37220222203558
     * 2024-dsg116-商家新增仓库
     */
    @PostMapping("/warehouses")
    @Audit(departName = "shops")
    public ReturnObject createWarehouse(@PathVariable Long shopId,
                                        @RequestBody WarehouseDto warehouseDto,
                                        @LoginUser UserDto user){
        Warehouse warehouse = CloneFactory.copy(new Warehouse(), warehouseDto);
        warehouse.setShopId(shopId);
        warehouse.setSenderMobile(warehouseDto.getSenderMobile());
        warehouse = warehouseService.createWarehouse(warehouse, user);
        return new ReturnObject(ReturnNo.CREATED, CloneFactory.copy(new WarehouseVo(), warehouse));
    }

    /**
     * @author 37220222203558
     * 2024-dsg116-商家修改仓库
     */
    @PutMapping("/warehouses/{id}")
    @Audit(departName = "shops")
    public ReturnObject changeWarehouse(@PathVariable Long shopId,
                                        @PathVariable Long id,
                                        @RequestBody WarehouseDto warehouseDto,
                                        @LoginUser UserDto user){
        Warehouse warehouse = CloneFactory.copy(new Warehouse(), warehouseDto);
        warehouse.setId(id);
        warehouse.setShopId(shopId);
        warehouseService.changeWarehouse(warehouse, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * @author 37220222203558
     * 2024-dsg116-商家删除仓库
     */
    @DeleteMapping("/warehouses/{id}")
    @Audit(departName = "shops")
    public ReturnObject deleteWarehouse(@PathVariable Long shopId,
                                        @PathVariable Long id,
                                        @LoginUser UserDto user){
        warehouseService.deleteWarehouse(shopId, id, user);
        return new ReturnObject();
    }

    /**
     * @author 37220222203558
     * 2024-dsg117-商户新增仓库配送地区
     */
    @PostMapping("/warehouses/{wid}/regions/{id}")
    @Audit(departName = "shops")
    public ReturnObject addWareHouseRegion(@PathVariable Long shopId,
                                           @PathVariable Long wid,
                                           @PathVariable Long id,
                                           @Validated(NewGroup.class)@RequestBody WarehouseRegionDto warehouseRegionDto,
                                           @LoginUser UserDto user){
        WarehouseRegion warehouseRegion = CloneFactory.copy(new WarehouseRegion(), warehouseRegionDto);
        warehouseRegion.setWarehouseId(wid);
        warehouseRegion.setRegionId(id);
        warehouseRegion = warehouseService.createWarehouseRegion(shopId, warehouseRegion, user);
        return new ReturnObject(ReturnNo.CREATED, CloneFactory.copy(new WarehouseRegionVo(), warehouseRegion));
    }

    /**
     * @author 37220222203558
     * 2024-dsg117-商户修改仓库配送地区
     */
    @PutMapping("/warehouses/{wid}/regions/{id}")
    @Audit(departName = "shops")
    public ReturnObject changeWareHouseRegion(@PathVariable Long shopId,
                                              @PathVariable Long wid,
                                              @PathVariable Long id,
                                              @Validated(UpdateGroup.class)@RequestBody WarehouseRegionDto warehouseRegionDto,
                                              @LoginUser UserDto user){
        WarehouseRegion warehouseRegion = CloneFactory.copy(new WarehouseRegion(), warehouseRegionDto);
        warehouseRegion.setRegionId(id);
        warehouseRegion.setWarehouseId(wid);
        warehouseService.changeWarehouseRegion(warehouseRegion, user);
        return new ReturnObject();
    }

    /**
     * @author 37220222203558
     * 2024-dsg117-商户或管理员取消仓库对某个地区的配送
     */
    @DeleteMapping("/warehouses/{wid}/regions/{id}")
    @Audit(departName = "shops")
    public ReturnObject delWareHouseRegion(@PathVariable Long shopId,
                                           @PathVariable Long wid,
                                           @PathVariable Long id,
                                           @LoginUser UserDto user){
        warehouseService.deleteWarehouseRegion(wid, id);
        return new ReturnObject();
    }

    /**
     * @author 37220222203558
     * 2024-dsg117-商户或管理员查询某个仓库的配送地区
     */
    @GetMapping("/warehouses/{id}/regions")
    @Audit(departName = "shops")
    public ReturnObject getWareHouseRegion(@PathVariable Long shopId,
                                           @PathVariable Long id,
                                           @RequestParam(required = false, defaultValue = "1") Integer page,
                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                           @LoginUser UserDto user){
        List<Region> regionList = warehouseService.getWarehouseRegions(shopId, id, page, pageSize);
        List<RegionVo> voList = regionList.stream()
                .map(region -> CloneFactory.copy(new RegionVo(), region))
                .collect(java.util.stream.Collectors.toList());
        return new ReturnObject(voList);
    }
}
