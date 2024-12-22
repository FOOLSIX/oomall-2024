package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.comment.controller.dto.CommentDto;
import cn.edu.xmu.oomall.comment.controller.vo.CommentVo;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import cn.edu.xmu.oomall.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CommentService commentService;

    /**
     * 获得当前用户所有评论
     * @param page
     * @param pageSize
     * @return
     */

    @GetMapping("/comments")
    public ReturnObject retrieveComments(@RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) Byte status,
                                         @LoginUser UserDto userDto) {
        Long uid = userDto.getId();
        if (Objects.isNull(status)) {
            status = Comment.REVIEWED;
        }
        List<Comment> bos = commentService.retrieveCommentsByUserIdAndStatus(uid, status,page, pageSize);
        return new ReturnObject(ReturnNo.OK, bos.stream().map(CommentVo::new).toList());
    }

    /**
     * 根据订单创建评论
     * @param orderId
     * @param userDto
     * @return
     */

    @PostMapping("/orders/{orderId}/comment")
    public ReturnObject createComment(@PathVariable Long orderId,
                                      @RequestBody CommentDto commentDto,
                                      @LoginUser UserDto userDto) {
        return new ReturnObject();//todo
    }

    /**
     * 根据评论创建追评
     * @param id 评论id
     * @return
     */

    @PostMapping("/comments/{id}/additional")
    public ReturnObject createAdditionalComment(@PathVariable Long id,
                                                @RequestBody CommentDto commentDto,
                                                @LoginUser UserDto userDto) {
        Comment comment = CloneFactory.copy(new Comment(), commentDto);
        commentService.createAdditionalComment(id, comment, userDto);
        return new ReturnObject(ReturnNo.OK);
    }
}
