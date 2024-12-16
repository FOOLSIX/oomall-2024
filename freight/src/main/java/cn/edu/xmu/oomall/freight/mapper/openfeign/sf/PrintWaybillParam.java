package cn.edu.xmu.oomall.freight.mapper.openfeign.sf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 打印面单的返回值
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 */
@Data
@NoArgsConstructor
public class PrintWaybillParam {
    /**
     * 面单模板，定死就好了
     */
    @JsonProperty("templateCode")
    private String templateCode = "fm_150_standard_YZZNLWLM";

    /**
     * 面单接口版本，模拟顺丰只实现了2.0
     */
    @JsonProperty("version")
    private String version = "2.0";

    /**
     * 文件类型，定死pdf
     */
    @JsonProperty("fileType")
    private String fileType = "pdf";

    /**
     * 定死采用同步调用
     */
    @JsonProperty("sync")
    private Boolean sync = true;

    /**
     * 运单列表
     */
    @JsonProperty("documents")
    private List<DocumentsDTO> documents;

    @NoArgsConstructor
    @Data
    public static class DocumentsDTO {
        /**
         * 运单号
         */
        @JsonProperty("masterWaybillNo")
        private String masterWaybillNo;
    }
}
