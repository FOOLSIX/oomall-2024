package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    /**
     * 获得当前用户所有评论
     * @param page
     * @param pageSize
     * @return
     */

    @GetMapping("/comments")
    public ReturnObject retrieveComments(@RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                         @LoginUser UserDto userDto) {
        return new ReturnObject();//todo
    }

    /**
     * 根据订单创建评论
     * @param orderId
     * @param userDto
     * @return
     */

    @PostMapping("/comments/{orderId}")
    public ReturnObject createComment(@PathVariable Long orderId,
                                      @LoginUser UserDto userDto) {
        return new ReturnObject();//todo
    }

    /**
     * 根据评论创建追评
     * @param id 评论id
     * @return
     */

    @PostMapping("/comments/{id}/additional")
    public ReturnObject createAdditionalComment(@PathVariable Long id) {
        return new ReturnObject();//todo
    }
}
