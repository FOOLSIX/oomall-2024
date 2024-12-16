package cn.edu.xmu.oomall.sfexpress.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 打印面单请求参数
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class PostPrintWaybillsDTO {

    @JsonProperty("templateCode")
    private String templateCode = "fm_150_standard_YZZNLWLM";
    @JsonProperty("version")
    private String version = "2.0";
    @JsonProperty("fileType")
    private String fileType = "pdf";
    @JsonProperty("sync")
    private Boolean sync = true;
    @JsonProperty("documents")
    private List<DocumentsDTO> documents;

    @NoArgsConstructor
    @Data
    public static class DocumentsDTO {
        @JsonProperty("masterWaybillNo")
        private String masterWaybillNo;
    }
}
