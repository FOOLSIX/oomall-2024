package cn.edu.xmu.oomall.comment.service;

import cn.edu.xmu.oomall.comment.dao.CommentDao;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class CommentService {
    private final static Logger logger = LoggerFactory.getLogger(CommentService.class);
    private final CommentDao commentDao;

    public void deleteById(Long id) {

    }

    public void approve(Long id) {}

    public void ban(Long id) {}


}
