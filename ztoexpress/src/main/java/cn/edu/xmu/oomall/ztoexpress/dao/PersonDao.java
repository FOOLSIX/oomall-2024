package cn.edu.xmu.oomall.ztoexpress.dao;

import cn.edu.xmu.oomall.ztoexpress.unit.PersonInfo;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RefreshScope
@Transactional
public class PersonDao {
    private PersonInfo personInfo;
}
