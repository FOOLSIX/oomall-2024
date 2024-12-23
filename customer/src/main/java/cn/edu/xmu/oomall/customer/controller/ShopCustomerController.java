package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.customer.controller.vo.CustomerVo;
import cn.edu.xmu.oomall.customer.controller.vo.SimpleCustomerVo;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j
public class ShopCustomerController {
    // 管理员-顾客
    private JwtHelper jwtHelper;
    private final CustomerService customerService;

    /**
     * 管理员获取指定用户信息
     *
     * @param id
     * @param shopId
     * @return
     */
    @GetMapping("shops/{shopId}/customer/{id}")
    @Audit(departName = "customer")
    public ReturnObject getUserById(@PathVariable Long shopId,
                                    @PathVariable Long id) {
        if (!shopId.equals(0L)) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }
        if (id == -1L) { // 检查特殊值，模拟 null
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, "填入的id参数为空");
        }
        Customer customer = this.customerService.getUserById(id);
        CustomerVo customerVo = CloneFactory.copy(new CustomerVo(), customer);
        return new ReturnObject(customerVo);
    }

    /**
     * 管理员获取所有用户信息
     *
     * @param shopId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/shops/{shopId}/customers")
    @Audit(departName = "customer")
    public ReturnObject getAllUsers(@PathVariable Long shopId,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        if (!shopId.equals(0L)) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }
        List<Customer> customers = this.customerService.getAllUsers(page, pageSize);
        List<SimpleCustomerVo> simpleCustomerVos = customers.stream().
                map(customer -> CloneFactory.copy(new SimpleCustomerVo(), customer)).collect(Collectors.toList());
        return new ReturnObject(new PageVo<>(simpleCustomerVos, page, pageSize));

    }

    /**
     * 管理员删除顾客
     *
     * @param id
     * @param shopId
     * @param userDto
     * @return
     */
    @DeleteMapping("/shops/{shopId}/customers/{id}")
    @Audit(departName = "customer")
    public ReturnObject delUserById(@PathVariable Long id,
                                    @PathVariable Long shopId,
                                    @LoginUser UserDto userDto) {
        if (!shopId.equals(0L)) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }
        this.customerService.delUserById(id, userDto);
        return new ReturnObject();
    }

    /**
     * 封禁顾客
     *
     * @param id
     * @param shopId
     * @param userDto
     * @return
     */
    @PutMapping("/shops/{shopId}/customers/{id}/ban")
    @Audit(departName = "customers")
    public ReturnObject banUser(@PathVariable Long id,
                                @PathVariable Long shopId,
                                @LoginUser UserDto userDto) {
        if (!shopId.equals(0L)) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }
        this.customerService.banUser(id, userDto);
        return new ReturnObject();
    }

    /**
     * 解禁顾客
     *
     * @param id
     * @param shopId
     * @param userDto
     * @return
     */
    @PutMapping("/shops/{shopId}/customers/{id}/release")
    @Audit(departName = "customers")
    public ReturnObject releaseUser(@PathVariable Long id,
                                    @PathVariable Long shopId,
                                    @LoginUser UserDto userDto) {
        if (!shopId.equals(0L)) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }
        this.customerService.releaseUser(id, userDto);
        return new ReturnObject();
    }
}
