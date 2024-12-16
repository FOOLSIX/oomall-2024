模拟极兔

实现功能：

1、实现极兔创建订单接口

2、实现极兔创建订单（带运单号）接口

3、实现极兔取消订单接口

4、实现极兔查询物流轨迹接口



未实现功能：

1、ApiAccount的digest校验

2、Customer的digest校验



轨迹实现逻辑

在创建订单的时候如果有传递进来上门取件时间，且早于当前时间，则立即取件，将订单状态设置为已取件

调用函数生成物流轨迹，使用随机数以1%丢件，1%问题件，98%正常配送

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


