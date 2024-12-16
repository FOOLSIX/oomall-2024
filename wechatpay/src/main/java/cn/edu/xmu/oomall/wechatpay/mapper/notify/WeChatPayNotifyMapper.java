package cn.edu.xmu.oomall.wechatpay.mapper.notify;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.wechatpay.mapper.notify.po.PaymentNotifyPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author maguoqi
 * @date 2023/12/5
 */
@Component
public class WeChatPayNotifyMapper
{
    private final RestTemplate restTemplate;

    @Autowired
    public WeChatPayNotifyMapper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ReturnObject notifyPayment(String notifyUrl, PaymentNotifyPo paymentNotifyPo) {
        ResponseEntity<ReturnObject> response = restTemplate.postForEntity("http://payment-service" + notifyUrl, paymentNotifyPo, ReturnObject.class);
        return response.getBody();
    }

}
