package cn.edu.xmu.oomall.jtexpress.controller;


import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.jtexpress.controller.dto.AddOrderReturnObj;
import cn.edu.xmu.oomall.jtexpress.controller.dto.CancelOrderReturnObj;
import cn.edu.xmu.oomall.jtexpress.controller.dto.ReturnResult;
import cn.edu.xmu.oomall.jtexpress.controller.dto.TracesReturnObj;
import cn.edu.xmu.oomall.jtexpress.controller.vo.CancelOrderVo;
import cn.edu.xmu.oomall.jtexpress.controller.vo.OrderVo;
import cn.edu.xmu.oomall.jtexpress.dao.TraceDetailDao;
import cn.edu.xmu.oomall.jtexpress.dao.bo.Order;
import cn.edu.xmu.oomall.jtexpress.dao.bo.PersonInfo;
import cn.edu.xmu.oomall.jtexpress.dao.bo.TraceDetail;
import cn.edu.xmu.oomall.jtexpress.exception.JTException;
import cn.edu.xmu.oomall.jtexpress.exception.ReturnError;
import cn.edu.xmu.oomall.jtexpress.service.ApiAccountService;
import cn.edu.xmu.oomall.jtexpress.service.OrderService;
import cn.edu.xmu.oomall.jtexpress.util.BeanValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 徐森彬
 * 2023-dgn3-02
 */
@RestController
@RequestMapping("/webopenplatformapi/api")
public class JTExpressController {
    private final static Logger logger = LoggerFactory.getLogger(JTExpressController.class);
    @Autowired
    private ApiAccountService apiAccountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TraceDetailDao traceDetailDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "order/addOrder", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ReturnResult addOrder(@RequestHeader("apiAccount") Long account, @RequestHeader(value = "digest",required = false) String digest, @RequestHeader("timestamp") long timestamp, @RequestParam("bizContent") String bizContent) throws JsonProcessingException {
        OrderVo orderVo = objectMapper.readValue(bizContent, OrderVo.class);
        BeanValidator.validate(orderVo);
        Order order = CloneFactory.copy(new Order(), orderVo);
        order.setSender(CloneFactory.copy(new PersonInfo(),orderVo.getSender()));
        order.setReceiver(CloneFactory.copy(new PersonInfo(),orderVo.getReceiver()));
        order = orderService.addOrder(order);
        return new ReturnResult(ReturnError.SUCCESS, AddOrderReturnObj.builder().billCode(order.getBillCode()).txlogisticId(order.getTxLogisticId()).build());
    }

    @PostMapping(value = "order/v2/addOrder", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ReturnResult addOrderWithBillCode(@RequestHeader("apiAccount") Long account, @RequestHeader(value = "digest",required = false) String digest, @RequestHeader("timestamp") long timestamp, @RequestParam("bizContent") String bizContent) throws JsonProcessingException {
        OrderVo orderVoV2 = objectMapper.readValue(bizContent, OrderVo.class);
        BeanValidator.validate(orderVoV2);
        Order order = CloneFactory.copy(new Order(), orderVoV2);
        order.setSender(CloneFactory.copy(new PersonInfo(),orderVoV2.getSender()));
        order.setReceiver(CloneFactory.copy(new PersonInfo(),orderVoV2.getReceiver()));
        order = orderService.addOrder(order);
        return new ReturnResult(ReturnError.SUCCESS, AddOrderReturnObj.builder().billCode(order.getBillCode()).txlogisticId(order.getTxLogisticId()).build());
    }

    @PostMapping(value = "order/cancelOrder", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ReturnResult cancelOrder(@RequestHeader("apiAccount") Long account, @RequestHeader(value = "digest",required = false) String digest, @RequestHeader("timestamp") long timestamp, @RequestParam("bizContent") String bizContent) throws JsonProcessingException {

        CancelOrderVo cancelOrderVo = objectMapper.readValue(bizContent, CancelOrderVo.class);
        BeanValidator.validate(cancelOrderVo);
        Order order = orderService.cancelOrder(cancelOrderVo.getCustomerCode(),cancelOrderVo.getTxLogisticId());
        ReturnResult r = new ReturnResult(ReturnError.SUCCESS, CancelOrderReturnObj.builder().billCode(order.getBillCode()).txlogisticId(order.getTxLogisticId()).build());
        return new ReturnResult(ReturnError.SUCCESS, CancelOrderReturnObj.builder().billCode(order.getBillCode()).txlogisticId(order.getTxLogisticId()).build());
    }

    @PostMapping(value = "logistics/trace", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ReturnResult getTraces(@RequestHeader("apiAccount") Long account, @RequestHeader(value = "digest",required = false) String digest, @RequestHeader("timestamp") long timestamp, @RequestParam("bizContent") String bizContent) throws JsonProcessingException {
        String[] billCodesStr = objectMapper.readTree(bizContent).get("billCodes").asText().split("[,，]");
        Set<String> billCodes = new HashSet<>(Arrays.asList(billCodesStr));
        if (billCodes.size() > 30) throw new JTException(ReturnError.BILL_CODE_EXCEED_30);
        List<TracesReturnObj> tracesReturnObjs = billCodes.stream().map(billCode -> {
            ArrayList<TraceDetail> traceDetails = traceDetailDao.getTrace(billCode);
            return (traceDetails != null) ? new TracesReturnObj(billCode, traceDetails) : null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return new ReturnResult(ReturnError.SUCCESS, tracesReturnObjs);
    }


}
