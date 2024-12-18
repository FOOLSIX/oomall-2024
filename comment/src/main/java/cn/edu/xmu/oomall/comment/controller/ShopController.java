package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("shops/{shopId}")
@RequiredArgsConstructor
public class ShopController {

    /**
     * 创建回复
     * @param shopId
     * @param id
     * @param userDto
     * @return
     */
    @PostMapping("comments/{id}/reply")
    @Audit(departName = "shops")
    public ReturnObject createReply(@PathVariable Long shopId,
                                    @PathVariable Long id,
                                    @LoginUser UserDto userDto) {
        return new ReturnObject();//todo
    }

    /**
     * 修改回复
     * @param id
     * @param userDto
     * @return
     */
    @PostMapping("comments/{id}")
    @Audit(departName = "shops")
    public ReturnObject updateReply(@PathVariable Long id,
                                    @LoginUser UserDto userDto) {

        return new ReturnObject();//todo
    }

    /**
     * 删除回复
     * @param id
     * @param userDto
     * @return
     */

    @DeleteMapping("comments/{id}")
    @Audit(departName = "shops")
    public ReturnObject deleteReply(@PathVariable Long id,
                                    @LoginUser UserDto userDto) {
        return new ReturnObject();//todo
    }


    @PostMapping("comments/{id}/block")
    @Audit(departName = "shops")
    public ReturnObject blockComment(@PathVariable Long id,
                                    @LoginUser UserDto userDto) {
        return new ReturnObject();//todo
    }
}
