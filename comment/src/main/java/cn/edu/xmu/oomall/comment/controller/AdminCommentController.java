package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.comment.controller.vo.CommentVo;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import cn.edu.xmu.oomall.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@RestController
@RequestMapping(value = "/platforms/{did}", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping("/comments/{id}")
    @Audit(departName = "platforms")
    public ReturnObject findCommentById(@PathVariable Long id,
                                      @PathVariable Long did,
                                      @LoginUser UserDto userDto) {
        CommentVo ret = new CommentVo(commentService.findCommentById(id));
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @DeleteMapping("/comments/{id}")
    @Audit(departName = "platforms")
    public ReturnObject deleteComment(@PathVariable Long id,
                                      @PathVariable Long did,
                                      @LoginUser UserDto userDto) {
        commentService.deleteById(id);
        return new ReturnObject(ReturnNo.OK);
    }

    @GetMapping("/comments")
    @Audit(departName = "platforms")
    public ReturnObject getComments(@PathVariable Long did,
                                    @RequestParam(required = false) Byte status,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                    @LoginUser UserDto userDto) {
        if (Objects.isNull(status)) {
            status = Comment.PENDING;
        }
        List<Comment> bos = commentService.retrieveCommentsByStatus(status, page, pageSize);
        return new ReturnObject(ReturnNo.OK, bos.stream().map(CommentVo::new).toList());

    }

    @PutMapping("/comments/{id}/approve")
    @Audit(departName = "platforms")
    public ReturnObject approveComment(@PathVariable Long id,
                                       @PathVariable Long did,
                                       @LoginUser UserDto userDto) {
        commentService.approve(id, userDto);
        return new ReturnObject(ReturnNo.OK);
    }

    @PutMapping("/comments/{id}/ban")
    @Audit(departName = "platforms")
    public ReturnObject banComment(@PathVariable Long id,
                                   @PathVariable Long did,
                                   @LoginUser UserDto userDto) {
        commentService.ban(id, userDto);
        return new ReturnObject(ReturnNo.OK);
    }
}
