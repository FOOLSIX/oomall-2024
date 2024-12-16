package cn.edu.xmu.oomall.alipay.util;

public class ParseToken {
    public static String parseUserAccount(String authorization) {
        String[] parts = authorization.split(",");
        for (String part : parts) {
            if (part.contains("app_cert_sn")) {
                return part.split("=")[1].trim();
            }
        }
        return "";
    }
    public static String parseAppId(String authorization) {
        String[] parts = authorization.split(",");
        for (String part : parts) {
            if (part.contains("app_id")) {
                return part.split("=")[1].trim();
            }
        }
        return "";
    }
}
