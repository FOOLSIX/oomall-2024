package cn.edu.xmu.oomall.sfexpress.controller.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 打印面单返回参数
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class PostPrintWaybillsRetVo {

    @JsonProperty("obj")
    private ObjDTO obj;
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("success")
    private Boolean success;

    @NoArgsConstructor
    @Data
    public static class ObjDTO {
        @JsonProperty("clientCode")
        private String clientCode = "YZZNLWLM";
        @JsonProperty("fileType")
        private String fileType = "pdf";
        @JsonProperty("files")
        private List<FilesDTO> files;
        @JsonProperty("templateCode")
        private String templateCode = "fm_150_standard_YZZNLWLM";

        @NoArgsConstructor
        @Data
        public static class FilesDTO {
            @JsonProperty("areaNo")
            private Integer areaNo = 1;
            @JsonProperty("documentSize")
            private Integer documentSize = 0;
            @JsonProperty("pageCount")
            private Integer pageCount = 0;
            @JsonProperty("pageNo")
            private Integer pageNo = 1;
            @JsonProperty("seqNo")
            private Integer seqNo = 1;
            @JsonProperty("token")
            private String token = "AUTH_tkv12_f146d1855480549d262b5c46ab0ab597ff20a97d9d0db45c16bedeb4fabd112b012deadd477ee524b1d690ce01baa3cdffbb125a6ccf69b73778dba2eb5157ebc9dc13c4c4c0ba1141595443b2dd5b14d6cf851262da99f41cda14fe21092c7944b49ec03a1d1d65faf787a9388a6620a42a6ded54ddb5cffedf74a116775d90b4a2595b877b38a234abab08910c80e0b56f9d2c739846ff3fb486ea0c418b07976be090344abfa3cbc2e7561443a5c3";
            @JsonProperty("url")
            private String url = "https://eos-scp-core-shenzhen-futian1-oss.sf-express.com:443/v1.2/AUTH_EOS-SCP-CORE/print-file-sbox/AAABjF5R1tTa77dE_uFJFaScRmn7AMmS_SF7444474753803_fm_150_standard_YZZNLWLM_1_1.pdf";
            @JsonProperty("waybillNo")
            private String waybillNo;
        }
    }
}
