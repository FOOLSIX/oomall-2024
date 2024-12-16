package cn.edu.xmu.oomall.ztoexpress.controller;

import cn.edu.xmu.oomall.ztoexpress.controller.dto.CreateOrderDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.GetOrderInfoDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.QueryTrackDto;
import cn.edu.xmu.oomall.ztoexpress.unit.PersonInfo;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.http.param.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ZTOExpressControllerTest {
    @Autowired
    private MockMvc mvc;

    //成功创建订单
    @Test
    void createZtoOrder1() throws Exception{
        PersonInfo senderInfo = new PersonInfo("张三","12345678901","江苏省","南京市","江宁区","东南大学");
        PersonInfo receiverInfo = new PersonInfo("李四","12345678901","江苏省","南京市","江宁区","南京大学");
        CreateOrderDto createOrderDto = new CreateOrderDto("1","1","nia222o",senderInfo,receiverInfo);
        MvcResult mvcResult= mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.createOrder")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(createOrderDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..message").value("请求成功"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("SYS000"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //创建订单时发现合作商订单号已经存在
    @Test
    void createZtoOrder2() throws Exception{
        PersonInfo senderInfo = new PersonInfo("张三","12345678901","江苏省","南京市","江宁区","东南大学");
        PersonInfo receiverInfo = new PersonInfo("李四","12345678901","江苏省","南京市","江宁区","南京大学");
        CreateOrderDto createOrderDto = new CreateOrderDto("1","1","PartnerCode4",senderInfo,receiverInfo);
        MvcResult mvcResult= mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.createOrder")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(createOrderDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..message").value("没有找到可以处理的订单类型"))
                .andExpect(MockMvcResultMatchers.jsonPath("$..status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("p-o001"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //创建订单时发现合作商订单号为空
    @Test
    void createZtoOrder3() throws Exception{
        PersonInfo senderInfo = new PersonInfo("张三","12345678901","江苏省","南京市","江宁区","东南大学");
        PersonInfo receiverInfo = new PersonInfo("李四","12345678901","江苏省","南京市","江宁区","南京大学");
        CreateOrderDto createOrderDto = new CreateOrderDto("1","1",null,senderInfo,receiverInfo);
        MvcResult mvcResult= mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.createOrder")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(createOrderDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("没有找到可以处理的订单类型"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("p-o001"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //正常获取订单信息
    @Test
    void getOrderInfo1() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1","ZT1743827865978880006898","null");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.getOrderInfo")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("请求成功"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("SYS000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.orderBo.orderPo.orderCode").value("ZT1743827865978880006898"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //获取订单信息时订单号和运单号均是空
    @Test
    void getOrderInfo2() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1",null,null);
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.getOrderInfo")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("XX不能为空"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("S208"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //获取订单时订单号和运单号非空，但是实际没有订单的信息
    @Test
    void getOrderInfo3() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1","nullnull","nullnull");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.getOrderInfo")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("鉴权失败,未绑定电子面单账号"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("E404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //订单号为空，运单不为空,但是找不到相应的运单
    @Test
    void getOrderInfo4() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1",null,"notnull");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.getOrderInfo")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("鉴权失败,未绑定电子面单账号"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("E404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //订单号为空，运单不为空,找到了运单，且找到相应的订单
    @Test
    void getOrderInfo5() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1",null,"ZT1743827865978880006899");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.getOrderInfo")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("请求成功"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("SYS000"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //订单号为空，运单不为空,找到了运单，但是找不到相应的订单
    @Test
    void getOrderInfo6() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1",null,"ZT1743827865978880006905");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.getOrderInfo")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("鉴权失败,未绑定电子面单账号"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("E404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //订单号和运单号都非空,并且当前订单状态不是已取消
    @Test
    void cancelPreOrder1() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1","ZT1743827865978880006896","1");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.cancelPreOrder")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("请求成功"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("SYS000"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //订单号和运单号都是空
    @Test
    void cancelPreOrder2() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1",null,null);
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.cancelPreOrder")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("XX不能为空"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("S208"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //通过订单号找到订单，但是当前订单的状态已经是已取消的了
    @Test
    void cancelPreOrder3() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1","ZT1743827865978880006897",null);
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.cancelPreOrder")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("参数校验失败"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("E403"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //订单号为空，通过运单号来取消订单
    @Test
    void cancelPreOrder4() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1",null,"ZT1743827865978880006899");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.cancelPreOrder")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("请求成功"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("SYS000"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //订单号为空，通过运单号找不到运单
    @Test
    void cancelPreOrder5() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1",null,"notnull");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.cancelPreOrder")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("鉴权失败,未绑定电子面单账号"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("E404"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //订单号为空，通过运单号找到订单，但是订单本身状态是已取消的
    @Test
    void cancelPreOrder6() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1",null,"ZT1743827865978880006897");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.cancelPreOrder")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("参数校验失败"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("E403"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    //订单号为空，通过运单号找到运单，通过运单号鉴权失败,未绑定电子面单账号
    @Test
    void cancelPreOrder7() throws Exception{
        GetOrderInfoDto getOrderInfoDto = new GetOrderInfoDto("1",null,"ZT1743827865978880006905");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.open.cancelPreOrder")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(getOrderInfoDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("鉴权失败,未绑定电子面单账号"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("E404"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //运单号存在且网点也存在
    @Test
    void queryTrack1() throws Exception{
        QueryTrackDto queryTrackDto =new QueryTrackDto("ZT1743827865978880006897");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.merchant.waybill.track.query")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(queryTrackDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("请求成功"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("SYS000"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //运单号存在但网点不存在
    @Test
    void queryTrack2() throws Exception{
        QueryTrackDto queryTrackDto =new QueryTrackDto("ZT1743827865978880006903");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.merchant.waybill.track.query")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(queryTrackDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("鉴权失败,未绑定电子面单账号"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("E404"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //运单号不存在
    @Test
    void queryTrack3() throws Exception{
        QueryTrackDto queryTrackDto =new QueryTrackDto("billCode");
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.merchant.waybill.track.query")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(queryTrackDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("鉴权失败,未绑定电子面单账号"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("E404"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //运单号为空
    @Test
    void queryTrack4() throws Exception{
        QueryTrackDto queryTrackDto =new QueryTrackDto(null);
        MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.post("/interal/zto.merchant.waybill.track.query")
                        .header("x-appKey","test")
                        .header("x-dataDigest","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(queryTrackDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("XX不能为空"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("S208"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}