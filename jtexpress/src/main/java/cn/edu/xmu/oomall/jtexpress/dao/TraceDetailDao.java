package cn.edu.xmu.oomall.jtexpress.dao;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.jtexpress.dao.bo.TraceDetail;
import cn.edu.xmu.oomall.jtexpress.mapper.jpa.TraceDetailPoMapper;
import cn.edu.xmu.oomall.jtexpress.mapper.po.TraceDetailPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

@Repository
@RefreshScope
@Transactional(propagation = Propagation.REQUIRED)
public class TraceDetailDao {
    @Autowired
    private TraceDetailPoMapper traceDetailPoMapper;

    public void createRandomTrace(String billCode) throws RuntimeException {
        LocalDateTime currentTime = LocalDateTime.now();
        TraceDetail traceDetail = new TraceDetail();
        traceDetail.setBillCode(billCode);
        //创建揽件的轨迹
        traceDetail.setScanType(TraceDetail.ScanType.EXPRESS_COLLECTION.getDescription());
        traceDetail.setScanTime(currentTime);
        traceDetailSave(traceDetail);


        //物流轨迹，1%的比例丢件，1%的比例不更新物流
        int randomNum = new Random().nextInt(100);
        //currentTime增加十秒，表示时间间隔
        currentTime = currentTime.plusSeconds(10);
        traceDetail.setScanTime(currentTime);
        if (randomNum == 0) return;
        if (randomNum > 1) {
            traceDetail.setScanType(TraceDetail.ScanType.EXPRESS_SIGN.getDescription());
        } else {
            int problemNum = new Random().nextInt(6) + 1;
            traceDetail.setScanType(TraceDetail.ScanType.PROBLEM_SCAN.getDescription());
            traceDetail.setProblemType(TraceDetail.ProblemType.problemTypeMap.get(problemNum));
        }
        traceDetailSave(traceDetail);

    }


    public void traceDetailSave(TraceDetail traceDetail) throws RuntimeException {
        TraceDetailPo traceDetailPo = CloneFactory.copy(new TraceDetailPo(), traceDetail);
        traceDetailPoMapper.save(traceDetailPo);
    }


    public ArrayList<TraceDetail> getTrace(String billCode) throws RuntimeException{
        if(billCode.length()>30)return null;

        ArrayList<TraceDetailPo> traceDetailPos = traceDetailPoMapper.findByBillCodeAndScanTimeBefore(billCode.trim(),LocalDateTime.now());

        // 判断结果是否为空
        if (traceDetailPos == null || traceDetailPos.isEmpty()) {
            return null;
        }

        // 转换并返回结果
        return traceDetailPos.stream()
                .map(o -> CloneFactory.copy(new TraceDetail(), o))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
