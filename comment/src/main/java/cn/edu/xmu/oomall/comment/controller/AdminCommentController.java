package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/platforms", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    @DeleteMapping("/comments/{id}")
    @Audit(departName = "platforms")
    public ReturnObject deleteComment(@PathVariable Long id, @LoginUser UserDto userDto) {
        commentService.deleteById(id);
        //todo
        return null;
    }

    @PostMapping("/comments/{id}/approve")
    @Audit(departName = "platforms")
    public ReturnObject approveComment(@PathVariable Long id, @LoginUser UserDto userDto) {
        return new ReturnObject();//todo
    }

    @PostMapping("/comments/{id}/ban")
    @Audit(departName = "platforms")
    public ReturnObject banComment(@PathVariable Long id, @LoginUser UserDto userDto) {
        return new ReturnObject();//todo
    }
}
