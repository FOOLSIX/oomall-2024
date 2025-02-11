package cn.edu.xmu.oomall.jtexpress.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonInfoDto {

    @NotNull
    @Size(max = 32)
    private String name;

    @Size(max = 100)
    private String company;

    @Size(max = 32)
    private String postCode;

    @Size(max = 150)
    private String mailBox;

    @Size(max = 30)
    private String mobile;

    @Size(max = 30)
    private String phone;

    @Size(max = 20)
    private String countryCode="CHN";

    @NotNull(message="地址信息不全")
    @Size(max = 32)
    private String prov;

    @NotNull(message="地址信息不全")
    @Size(max = 32)
    private String city;

    @NotNull(message="地址信息不全")
    @Size(max = 32)
    private String area;

    @Size(max = 32)
    private String town;

    @Size(max = 32)
    private String street;

    @NotNull(message="地址信息不全")
    @Size(max = 150)
    private String address;

}
