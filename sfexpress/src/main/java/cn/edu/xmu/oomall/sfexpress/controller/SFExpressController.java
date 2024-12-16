package cn.edu.xmu.oomall.sfexpress.controller;

import cn.edu.xmu.oomall.sfexpress.controller.dto.*;
import cn.edu.xmu.oomall.sfexpress.exception.SFErrorCodeEnum;
import cn.edu.xmu.oomall.sfexpress.controller.vo.*;
import cn.edu.xmu.oomall.sfexpress.service.SfexpressService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟顺丰控制器
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
//@Validated
@RestController
@RequestMapping(value = "/internal/sf", produces = "application/json;charset=UTF-8")
public class SFExpressController {

    private final SfexpressService sfexpressService;

    @Autowired
    public SFExpressController(SfexpressService sfexpressService) {
        this.sfexpressService = sfexpressService;
    }

    @PostMapping("/")
    public SFResponseVo SFServiceAdapter(@Valid @RequestBody SFPostRequestDTO<Object> sfPostRequestDTO) throws JsonProcessingException {
        String serviceCode = sfPostRequestDTO.getServiceCode();
        String partnerID = sfPostRequestDTO.getPartnerID();
        String requestID = sfPostRequestDTO.getRequestID();
        String timestamp = sfPostRequestDTO.getTimestamp();
        String msgDigest = sfPostRequestDTO.getMsgDigest();
        ObjectMapper objectMapper = new ObjectMapper();
        String msgDataJson = objectMapper.writeValueAsString(sfPostRequestDTO.getMsgData());

        switch (serviceCode) {
            case "EXP_RECE_CREATE_ORDER":
                // 创建返回对象
                SFResponseVo<PostCreateOrderRetVo> postCreateOrderRetVoSFResponseVo = new SFResponseVo<>();
                postCreateOrderRetVoSFResponseVo.setApiResultCode("A1000");
                postCreateOrderRetVoSFResponseVo.setApiErrorMsg("");
                postCreateOrderRetVoSFResponseVo.setApiResponseID(requestID);
                ApiResultData<PostCreateOrderRetVo> postCreateOrderRetVoApiResultData = new ApiResultData<>();
                // 参数校验
                PostCreateOrderDTO createOrderDTO = objectMapper.readValue(msgDataJson, PostCreateOrderDTO.class);
                List<ContactInfoListDTO> contactInfoList = createOrderDTO.getContactInfoList();
                postCreateOrderRetVoApiResultData.setSuccess(false); // 先设成false，如果校验全通过了就改成true
                if (contactInfoList.size() < 2) {
                    postCreateOrderRetVoApiResultData.setErrorCode(SFErrorCodeEnum.E1010.getErrorCodeString());
                    postCreateOrderRetVoApiResultData.setErrorMsg(SFErrorCodeEnum.E1010.getErrorDescAndAdvice());
                    postCreateOrderRetVoSFResponseVo.setApiResultData(postCreateOrderRetVoApiResultData);
                    postCreateOrderRetVoSFResponseVo.setApiResultCode("A1001");
                    postCreateOrderRetVoSFResponseVo.setApiErrorMsg("发件人和收件人信息不能为空");
                    return postCreateOrderRetVoSFResponseVo;
                }
                for (ContactInfoListDTO contactInfo : contactInfoList) {
                    if (contactInfo.getContactType() == 1) {
                        if (StringUtils.isBlank(contactInfo.getAddress())) {
                            postCreateOrderRetVoApiResultData.setErrorCode(SFErrorCodeEnum.E1010.getErrorCodeString());
                            postCreateOrderRetVoApiResultData.setErrorMsg(SFErrorCodeEnum.E1010.getErrorDescAndAdvice());
                            postCreateOrderRetVoSFResponseVo.setApiResultData(postCreateOrderRetVoApiResultData);
                            postCreateOrderRetVoSFResponseVo.setApiResultCode("A1001");
                            postCreateOrderRetVoSFResponseVo.setApiErrorMsg("发件人地址不能为空");
                            return postCreateOrderRetVoSFResponseVo;
                        }
                        if (StringUtils.isBlank(contactInfo.getContact())) {
                            postCreateOrderRetVoApiResultData.setErrorCode(SFErrorCodeEnum.E1011.getErrorCodeString());
                            postCreateOrderRetVoApiResultData.setErrorMsg(SFErrorCodeEnum.E1011.getErrorDescAndAdvice());
                            postCreateOrderRetVoSFResponseVo.setApiResultData(postCreateOrderRetVoApiResultData);
                            postCreateOrderRetVoSFResponseVo.setApiResultCode("A1001");
                            postCreateOrderRetVoSFResponseVo.setApiErrorMsg("发件联系人不能为空");
                            return postCreateOrderRetVoSFResponseVo;
                        }
                        if (StringUtils.isBlank(contactInfo.getMobile())) {
                            postCreateOrderRetVoApiResultData.setErrorCode(SFErrorCodeEnum.E1012.getErrorCodeString());
                            postCreateOrderRetVoApiResultData.setErrorMsg(SFErrorCodeEnum.E1012.getErrorDescAndAdvice());
                            postCreateOrderRetVoSFResponseVo.setApiResultData(postCreateOrderRetVoApiResultData);
                            postCreateOrderRetVoSFResponseVo.setApiResultCode("A1001");
                            postCreateOrderRetVoSFResponseVo.setApiErrorMsg("发件人电话不能为空");
                            return postCreateOrderRetVoSFResponseVo;
                        }
                    } else if (contactInfo.getContactType() == 2) {
                        if (StringUtils.isBlank(contactInfo.getAddress())) {
                            postCreateOrderRetVoApiResultData.setErrorCode(SFErrorCodeEnum.E1014.getErrorCodeString());
                            postCreateOrderRetVoApiResultData.setErrorMsg(SFErrorCodeEnum.E1014.getErrorDescAndAdvice());
                            postCreateOrderRetVoSFResponseVo.setApiResultData(postCreateOrderRetVoApiResultData);
                            postCreateOrderRetVoSFResponseVo.setApiResultCode("A1001");
                            postCreateOrderRetVoSFResponseVo.setApiErrorMsg("收件人地址不能为空");
                            return postCreateOrderRetVoSFResponseVo;
                        }
                        if (StringUtils.isBlank(contactInfo.getContact())) {
                            postCreateOrderRetVoApiResultData.setErrorCode(SFErrorCodeEnum.E1015.getErrorCodeString());
                            postCreateOrderRetVoApiResultData.setErrorMsg(SFErrorCodeEnum.E1015.getErrorDescAndAdvice());
                            postCreateOrderRetVoSFResponseVo.setApiResultData(postCreateOrderRetVoApiResultData);
                            postCreateOrderRetVoSFResponseVo.setApiResultCode("A1001");
                            postCreateOrderRetVoSFResponseVo.setApiErrorMsg("收件联系人不能为空");
                            return postCreateOrderRetVoSFResponseVo;
                        }
                        if (StringUtils.isBlank(contactInfo.getMobile())) {
                            postCreateOrderRetVoApiResultData.setErrorCode(SFErrorCodeEnum.E1016.getErrorCodeString());
                            postCreateOrderRetVoApiResultData.setErrorMsg(SFErrorCodeEnum.E1016.getErrorDescAndAdvice());
                            postCreateOrderRetVoSFResponseVo.setApiResultData(postCreateOrderRetVoApiResultData);
                            postCreateOrderRetVoSFResponseVo.setApiResultCode("A1001");
                            postCreateOrderRetVoSFResponseVo.setApiErrorMsg("收件人电话不能为空");
                            return postCreateOrderRetVoSFResponseVo;
                        }
                    }
                }

                postCreateOrderRetVoApiResultData.setSuccess(true);
                postCreateOrderRetVoApiResultData.setErrorCode("S0000");
                postCreateOrderRetVoApiResultData.setErrorMsg(null);

                PostCreateOrderRetVo newOrderVo = sfexpressService.createOrder(createOrderDTO);
                postCreateOrderRetVoApiResultData.setMsgData(newOrderVo);
                postCreateOrderRetVoSFResponseVo.setApiResultData(postCreateOrderRetVoApiResultData);
                return postCreateOrderRetVoSFResponseVo;

            case "EXP_RECE_SEARCH_ORDER_RESP":
                PostSearchOrderDTO searchOrderDTO = objectMapper.readValue(msgDataJson, PostSearchOrderDTO.class);
                PostSearchOrderRetVo orderVo = sfexpressService.searchOrder(searchOrderDTO);
                SFResponseVo<PostSearchOrderRetVo> postSearchOrderRetVoSFResponseVo = new SFResponseVo<>();
                postSearchOrderRetVoSFResponseVo.setApiResultCode("A1000");
                postSearchOrderRetVoSFResponseVo.setApiErrorMsg("");
                postSearchOrderRetVoSFResponseVo.setApiResponseID(requestID);
                ApiResultData<PostSearchOrderRetVo> postSearchOrderRetVoApiResultData = new ApiResultData<>();
                postSearchOrderRetVoApiResultData.setSuccess(true);
                postSearchOrderRetVoApiResultData.setErrorCode("S0000");
                postSearchOrderRetVoApiResultData.setErrorMsg(null);
                postSearchOrderRetVoApiResultData.setMsgData(orderVo);
                postSearchOrderRetVoSFResponseVo.setApiResultData(postSearchOrderRetVoApiResultData);
                return postSearchOrderRetVoSFResponseVo;

            case "EXP_RECE_SEARCH_ROUTES":
                PostSearchRoutesDTO searchRoutesDTO = objectMapper.readValue(msgDataJson, PostSearchRoutesDTO.class);
                PostSearchRoutesRetVo route = sfexpressService.searchRoutes(searchRoutesDTO);
                SFResponseVo<PostSearchRoutesRetVo> postSearchRoutesRetVoSFResponseVo = new SFResponseVo<>();
                postSearchRoutesRetVoSFResponseVo.setApiResultCode("A1000");
                postSearchRoutesRetVoSFResponseVo.setApiResponseID(requestID);
                postSearchRoutesRetVoSFResponseVo.setApiErrorMsg("");
                ApiResultData<PostSearchRoutesRetVo> postSearchRoutesRetVoApiResultData = new ApiResultData<>();
                postSearchRoutesRetVoApiResultData.setSuccess(true);
                postSearchRoutesRetVoApiResultData.setErrorCode("S0000");
                postSearchRoutesRetVoApiResultData.setErrorMsg(null);
                postSearchRoutesRetVoApiResultData.setMsgData(route);
                postSearchRoutesRetVoSFResponseVo.setApiResultData(postSearchRoutesRetVoApiResultData);
                return postSearchRoutesRetVoSFResponseVo;

            case "EXP_RECE_UPDATE_ORDER":
                PostUpdateOrderDTO updateOrderDTO = objectMapper.readValue(msgDataJson, PostUpdateOrderDTO.class);
                PostUpdateOrderRetVo updateOrderRetVo = sfexpressService.updateOrder(updateOrderDTO);
                SFResponseVo<PostUpdateOrderRetVo> postUpdateOrderRetVoSFResponseVo = new SFResponseVo<>();
                postUpdateOrderRetVoSFResponseVo.setApiResultCode("A1000");
                postUpdateOrderRetVoSFResponseVo.setApiResponseID(requestID);
                postUpdateOrderRetVoSFResponseVo.setApiErrorMsg("");
                ApiResultData<PostUpdateOrderRetVo> postUpdateOrderRetVoApiResultData = new ApiResultData<>();
                postUpdateOrderRetVoApiResultData.setSuccess(true);
                postUpdateOrderRetVoApiResultData.setErrorCode("S0000");
                postUpdateOrderRetVoApiResultData.setErrorMsg(null);
                postUpdateOrderRetVoApiResultData.setMsgData(updateOrderRetVo);
                postUpdateOrderRetVoSFResponseVo.setApiResultData(postUpdateOrderRetVoApiResultData);
                return postUpdateOrderRetVoSFResponseVo;

            case "COM_RECE_CLOUD_PRINT_WAYBILLS":
                PostPrintWaybillsDTO printWaybillsDTO = objectMapper.readValue(msgDataJson, PostPrintWaybillsDTO.class);
                List<PostPrintWaybillsRetVo.ObjDTO.FilesDTO> filesDTOList = new ArrayList<>();
                for (PostPrintWaybillsDTO.DocumentsDTO document : printWaybillsDTO.getDocuments()) {
                    PostPrintWaybillsRetVo.ObjDTO.FilesDTO filesDTO = new PostPrintWaybillsRetVo.ObjDTO.FilesDTO();
                    filesDTO.setWaybillNo(document.getMasterWaybillNo());
                    filesDTOList.add(filesDTO);
                }
                PostPrintWaybillsRetVo printWaybillsRetVo = new PostPrintWaybillsRetVo();
                PostPrintWaybillsRetVo.ObjDTO objDTO = new PostPrintWaybillsRetVo.ObjDTO();
                objDTO.setFiles(filesDTOList);
                printWaybillsRetVo.setObj(objDTO);

                SFResponseVo<PostPrintWaybillsRetVo> postPrintWaybillsRetVoSFResponseVo = new SFResponseVo<>();
                postPrintWaybillsRetVoSFResponseVo.setApiResultCode("A1000");
                postPrintWaybillsRetVoSFResponseVo.setApiResponseID(requestID);
                postPrintWaybillsRetVoSFResponseVo.setApiErrorMsg("");
                ApiResultData<PostPrintWaybillsRetVo> searchRoutesRetVoApiResultData = new ApiResultData<>();
                searchRoutesRetVoApiResultData.setSuccess(true);
                searchRoutesRetVoApiResultData.setErrorCode("S0000");
                searchRoutesRetVoApiResultData.setErrorMsg(null);
                searchRoutesRetVoApiResultData.setMsgData(printWaybillsRetVo);
                postPrintWaybillsRetVoSFResponseVo.setApiResultData(searchRoutesRetVoApiResultData);
                return postPrintWaybillsRetVoSFResponseVo;

            default:
                SFResponseVo sfResponseVo = new SFResponseVo<>();
                sfResponseVo.setApiErrorMsg("服务代码不存在");
                sfResponseVo.setApiResponseID(requestID);
                sfResponseVo.setApiResultCode("A1001");
                return sfResponseVo;
        }
    }
}
