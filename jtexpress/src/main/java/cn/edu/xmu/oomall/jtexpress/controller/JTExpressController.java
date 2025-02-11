package cn.edu.xmu.oomall.jtexpress.controller;


import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.jtexpress.controller.vo.AddOrderReturnVo;
import cn.edu.xmu.oomall.jtexpress.controller.vo.CancelOrderReturnVo;
import cn.edu.xmu.oomall.jtexpress.controller.vo.ReturnResultVo;
import cn.edu.xmu.oomall.jtexpress.controller.vo.TracesReturnVo;
import cn.edu.xmu.oomall.jtexpress.controller.dto.CancelOrderDto;
import cn.edu.xmu.oomall.jtexpress.controller.dto.OrderDto;
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
    public ReturnResultVo addOrder(@RequestHeader("apiAccount") Long account, @RequestHeader(value = "digest",required = false) String digest, @RequestHeader("timestamp") long timestamp, @RequestParam("bizContent") String bizContent) throws JsonProcessingException {
        OrderDto orderDto = objectMapper.readValue(bizContent, OrderDto.class);
        BeanValidator.validate(orderDto);
        Order order = CloneFactory.copy(new Order(), orderDto);
        order.setSender(CloneFactory.copy(new PersonInfo(), orderDto.getSender()));
        order.setReceiver(CloneFactory.copy(new PersonInfo(), orderDto.getReceiver()));
        order = orderService.addOrder(order);
        return new ReturnResultVo(ReturnError.SUCCESS, AddOrderReturnVo.builder().billCode(order.getBillCode()).txlogisticId(order.getTxLogisticId()).build());
    }

    @PostMapping(value = "order/v2/addOrder", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ReturnResultVo addOrderWithBillCode(@RequestHeader("apiAccount") Long account, @RequestHeader(value = "digest",required = false) String digest, @RequestHeader("timestamp") long timestamp, @RequestParam("bizContent") String bizContent) throws JsonProcessingException {
        OrderDto orderDtoV2 = objectMapper.readValue(bizContent, OrderDto.class);
        BeanValidator.validate(orderDtoV2);
        Order order = CloneFactory.copy(new Order(), orderDtoV2);
        order.setSender(CloneFactory.copy(new PersonInfo(), orderDtoV2.getSender()));
        order.setReceiver(CloneFactory.copy(new PersonInfo(), orderDtoV2.getReceiver()));
        order = orderService.addOrder(order);
        return new ReturnResultVo(ReturnError.SUCCESS, AddOrderReturnVo.builder().billCode(order.getBillCode()).txlogisticId(order.getTxLogisticId()).build());
    }

    @PostMapping(value = "order/cancelOrder", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ReturnResultVo cancelOrder(@RequestHeader("apiAccount") Long account, @RequestHeader(value = "digest",required = false) String digest, @RequestHeader("timestamp") long timestamp, @RequestParam("bizContent") String bizContent) throws JsonProcessingException {

        CancelOrderDto cancelOrderDto = objectMapper.readValue(bizContent, CancelOrderDto.class);
        BeanValidator.validate(cancelOrderDto);
        Order order = orderService.cancelOrder(cancelOrderDto.getCustomerCode(), cancelOrderDto.getTxLogisticId());
        ReturnResultVo r = new ReturnResultVo(ReturnError.SUCCESS, CancelOrderReturnVo.builder().billCode(order.getBillCode()).txlogisticId(order.getTxLogisticId()).build());
        return new ReturnResultVo(ReturnError.SUCCESS, CancelOrderReturnVo.builder().billCode(order.getBillCode()).txlogisticId(order.getTxLogisticId()).build());
    }

    @PostMapping(value = "logistics/trace", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ReturnResultVo getTraces(@RequestHeader("apiAccount") Long account, @RequestHeader(value = "digest",required = false) String digest, @RequestHeader("timestamp") long timestamp, @RequestParam("bizContent") String bizContent) throws JsonProcessingException {
        String[] billCodesStr = objectMapper.readTree(bizContent).get("billCodes").asText().split("[,，]");
        Set<String> billCodes = new HashSet<>(Arrays.asList(billCodesStr));
        if (billCodes.size() > 30) throw new JTException(ReturnError.BILL_CODE_EXCEED_30);
        List<TracesReturnVo> tracesReturnVos = billCodes.stream().map(billCode -> {
            ArrayList<TraceDetail> traceDetails = traceDetailDao.getTrace(billCode);
            return (traceDetails != null) ? new TracesReturnVo(billCode, traceDetails) : null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return new ReturnResultVo(ReturnError.SUCCESS, tracesReturnVos);
    }


}
