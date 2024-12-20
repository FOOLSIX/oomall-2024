package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@RestController
@RequestMapping(value = "/platforms/{did}", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    @DeleteMapping("/comments/{id}")
    @Audit(departName = "platforms")
    public ReturnObject deleteComment(@PathVariable Long id,
                                      @PathVariable Long did,
                                      @LoginUser UserDto userDto) {
        if (!PLATFORM.equals(did)) {
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT);
        }
        commentService.deleteById(id);
        return null;//todo need del?
    }

    @PostMapping("/comments/{id}/approve")
    @Audit(departName = "platforms")
    public ReturnObject approveComment(@PathVariable Long id,
                                       @PathVariable Long did,
                                       @LoginUser UserDto userDto) {
        if (!PLATFORM.equals(did)) {
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT);
        }
        commentService.approve(id, userDto);
        return new ReturnObject(ReturnNo.OK);
    }

    @PostMapping("/comments/{id}/ban")
    @Audit(departName = "platforms")
    public ReturnObject banComment(@PathVariable Long id,
                                   @PathVariable Long did,
                                   @LoginUser UserDto userDto) {
        if (!PLATFORM.equals(did)) {
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT);
        }
        commentService.ban(id, userDto);
        return new ReturnObject(ReturnNo.OK);
    }
}
