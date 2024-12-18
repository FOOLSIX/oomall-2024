package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UnAuthorizedController {

    /**
     * 获取商品的评论
     * @param productId
     * @param page
     * @param pageSize
     * @param userDto
     * @return
     */
    @GetMapping("/products/{productId}/comments")
    public ReturnObject retrieveCommentsByProductId(@PathVariable("productId") Long productId,
                                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                    @LoginUser UserDto userDto) {
        return new ReturnObject();//todo
    }
}
