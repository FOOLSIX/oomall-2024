package cn.edu.xmu.oomall.ztoexpress.unit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfo {
    private String name;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String address;

}
