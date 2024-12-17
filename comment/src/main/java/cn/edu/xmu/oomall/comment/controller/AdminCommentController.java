package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@RestController
@RequestMapping(value = "/platforms", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    @DeleteMapping("comments/{id}")
    public ReturnObject deleteComment(@PathVariable Long id, @LoginUser UserDto userDto) {
        if (!PLATFORM.equals(userDto.getDepartId())) {
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT, "平台管理员才可以删除评论");
        }
        commentService.deleteById(id);
        //todo
        return null;
    }
}
