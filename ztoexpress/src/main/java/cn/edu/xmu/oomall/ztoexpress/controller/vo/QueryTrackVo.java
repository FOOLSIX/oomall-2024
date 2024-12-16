package cn.edu.xmu.oomall.ztoexpress.controller.vo;

import cn.edu.xmu.oomall.ztoexpress.mapper.po.SitePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryTrackVo {
    private LocalDate scanDate;
    private SitePo sitePo;
}
