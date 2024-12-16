package cn.edu.xmu.oomall.jtexpress.mapper.jpa;

import cn.edu.xmu.oomall.jtexpress.mapper.po.ItemPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ItemPoMapper extends JpaRepository<ItemPo, Long> {

}
