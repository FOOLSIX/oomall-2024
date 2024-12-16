package cn.edu.xmu.oomall.ztoexpress.dao.bo;

import cn.edu.xmu.oomall.ztoexpress.mapper.po.ExpressPo;
import cn.edu.xmu.oomall.ztoexpress.mapper.po.SitePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@Repository
public class ExpressBo {
    private ExpressPo expressPo;
    private SitePo sitePo;
    public ExpressBo(){
        this.expressPo=new ExpressPo();
        this.sitePo=new SitePo();
    }
}
