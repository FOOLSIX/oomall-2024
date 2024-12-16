package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.dto.UndeliverableDto;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import cn.edu.xmu.oomall.freight.service.UndeliverableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/platforms/{shopId}", produces = "application/json;charset=UTF-8")
public class PlatformController {

    private final UndeliverableService undeliverableService;

    @PostMapping("/logistics/{id}/regions/{rid}/undeliverable")
    @Audit(departName = "platforms")
    public ReturnObject addUndeliverableRegion(@PathVariable Long shopId,
                                               @PathVariable Long id,
                                               @PathVariable Long rid,
                                               @RequestBody UndeliverableDto undeliverableDto,
                                               @LoginUser UserDto user) {
        // 仅平台管理员可以操作
        if (Objects.equals(PLATFORM, shopId)) {
            Undeliverable undeliverable = CloneFactory.copy(new Undeliverable(), undeliverableDto);
            undeliverable.setLogisticsId(id);
            undeliverable.setRegionId(rid);
            undeliverableService.addUndeliverableRegion(undeliverable, user);
        } else {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "未送达地区", rid, id));
        }
        return new ReturnObject(ReturnNo.CREATED);
    }

    @DeleteMapping("/logistics/{id}/regions/{rid}/undeliverable")
    @Audit(departName = "platforms")
    public ReturnObject deleteUndeliverableRegion(@PathVariable Long shopId,
                                                  @PathVariable Long rid,
                                                  @PathVariable Long id,
                                                  @LoginUser UserDto user) {
        if (Objects.equals(PLATFORM, shopId)) {
            undeliverableService.deleteUndeliverableRegion(rid, id, user);
        } else {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "未送达地区", rid, id));
        }
        return new ReturnObject(ReturnNo.OK);
    }

}
