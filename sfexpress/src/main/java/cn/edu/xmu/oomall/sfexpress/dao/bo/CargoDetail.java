package cn.edu.xmu.oomall.sfexpress.dao.bo;

import cn.edu.xmu.oomall.sfexpress.controller.dto.CargoDetailsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class CargoDetail {
    /**
     * 货物名称
     */
    private String name;

    public static List<CargoDetailsDTO> toDto(List<CargoDetail> cargoDetails) {
        List<CargoDetailsDTO> cargoDetailsDTOS = new ArrayList<>();
        for (CargoDetail cargoDetail : cargoDetails) {
            CargoDetailsDTO cargoDetailsDTO = new CargoDetailsDTO();
            BeanUtils.copyProperties(cargoDetail, cargoDetailsDTO);
            cargoDetailsDTOS.add(cargoDetailsDTO);
        }
        return cargoDetailsDTOS;
    }
}