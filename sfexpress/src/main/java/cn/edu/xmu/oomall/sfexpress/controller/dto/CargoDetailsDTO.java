package cn.edu.xmu.oomall.sfexpress.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.sfexpress.dao.bo.CargoDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 寄托物DTO
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class CargoDetailsDTO {
    @JsonProperty("name")
    @NotBlank(message = "1023")
    private String name;

    public static List<CargoDetail> toBo(List<CargoDetailsDTO> cargoDetailsDTOList) {
        List<CargoDetail> cargoDetailList = new ArrayList<>();
        for (CargoDetailsDTO cargoDetailsDTO : cargoDetailsDTOList) {
            CargoDetail cargoDetail = new CargoDetail();
            cargoDetail.setName(cargoDetailsDTO.getName());
            cargoDetailList.add(cargoDetail);
        }
        return cargoDetailList;
    }
}
