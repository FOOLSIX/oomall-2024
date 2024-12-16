package cn.edu.xmu.oomall.jtexpress.dao.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiAccount {
    private Long id;


    private Long account;


    private String privateKey;



    /**
     * 检查header中的digest是否正确
     *
     * @param digest
     * @param bizContent
     * @return
     * @throws NoSuchAlgorithmException
     */
//    public boolean checkDigest(String digest, String bizContent) throws NoSuchAlgorithmException {
//        String dataToSign = bizContent + privateKey;
//        // 使用MD5算法进行哈希运算
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        byte[] digestBytes = md.digest(dataToSign.getBytes());
//        // 对MD5哈希值进行Base64编码
//        String calculatedDigest = Base64.getEncoder().encodeToString(digestBytes);
//        return Objects.equals(digest, calculatedDigest);
//    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
