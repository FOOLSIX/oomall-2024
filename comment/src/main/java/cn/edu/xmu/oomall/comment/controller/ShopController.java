package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.comment.controller.dto.CommentDto;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import cn.edu.xmu.oomall.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("shops/{shopId}")
@RequiredArgsConstructor
public class ShopController {

    private final CommentService commentService;

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
                                    @RequestBody CommentDto commentDto,
                                    @LoginUser UserDto userDto) {
        return new ReturnObject();//todo
    }

    /**
     * 修改回复
     * @param id
     * @param userDto
     * @return
     */
    @PutMapping("comments/{id}")
    @Audit(departName = "shops")
    public ReturnObject updateReply(@PathVariable Long id,
                                    @RequestBody CommentDto commentDto,
                                    @LoginUser UserDto userDto) {
        Comment comment = CloneFactory.copy(new Comment(), commentDto);
        commentService.updateComment(comment, userDto);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 删除回复(不一定需要
     * @param id
     * @param userDto
     * @return
     */

    @DeleteMapping("comments/{id}")
    @Audit(departName = "shops")
    public ReturnObject deleteReply(@PathVariable Long id,
                                    @LoginUser UserDto userDto) {
        commentService.deleteById(id);
        return new ReturnObject(ReturnNo.OK);
    }


    @PutMapping("comments/{id}/block")
    @Audit(departName = "shops")
    public ReturnObject blockComment(@PathVariable Long id,
                                    @LoginUser UserDto userDto) {
        commentService.requestBlock(id, userDto);
        return new ReturnObject(ReturnNo.OK);
    }
}
