package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.vo.StatusVo;
import cn.edu.xmu.oomall.comment.controller.vo.CommentVo;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import cn.edu.xmu.oomall.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UnAuthorizedController {

    private final CommentService commentService;

    /**
     * 获取商品的可见评论
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
        List<Comment> bos = commentService.retrieveReviewedByProductId(productId, page, pageSize);
        return new ReturnObject(ReturnNo.OK, bos.stream().map(CommentVo::new).toList());
    }

    /**
     * 获取评论状态
     * @return
     */
    @GetMapping("/comments/states")
    public ReturnObject getRegionsState() {
        List<StatusVo> dtoList = this.commentService.retrieveCommentStates();
        return new ReturnObject(ReturnNo.OK, dtoList);
    }
}
