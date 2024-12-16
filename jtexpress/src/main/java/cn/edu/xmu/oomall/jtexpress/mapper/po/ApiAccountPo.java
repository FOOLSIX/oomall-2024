package cn.edu.xmu.oomall.jtexpress.mapper.po;


import jakarta.persistence.*;

@Entity
@Table(name = "jtexpress_api_account")
public class ApiAccountPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account")
    private Long account;

    @Column(name = "private_key", length = 50)
    private String privateKey;

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
