package cn.edu.xmu.oomall.sfexpress.dao.bo;


import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.sfexpress.SfExpressApplication;
import cn.edu.xmu.oomall.sfexpress.controller.dto.CargoDetailsDTO;
import cn.edu.xmu.oomall.sfexpress.controller.dto.ContactInfoListDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
   import static org.junit.jupiter.api.Assertions.assertEquals; // JUnit 5
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = SfExpressApplication.class)
@AutoConfigureMockMvc
//@Transactional
public class SfexpressBoTest {
    @Test
    void routeGeneratorTest() {
        List<Route> routes = RouteResps.generateRandomRouteList(3);
        System.out.println(Arrays.toString(routes.toArray()));
    }

    @Test
    void copytest(){
        ContactInfoListDTO contactInfoListDTO = new ContactInfoListDTO();
        contactInfoListDTO.setAddress("address");
        contactInfoListDTO.setMobile("mobile");
        contactInfoListDTO.setContact("contact");

        List<ContactInfoListDTO>contactInfoListDTOList = Arrays.asList(contactInfoListDTO);
        ContactInfo contactInfo = CloneFactory.copy(new ContactInfo(),contactInfoListDTOList.get(0));
        System.out.println(contactInfo.getAddress());
        System.out.println(contactInfo.getMobile());
        System.out.println(contactInfo.getContact());
    }

        private List<CargoDetail> cargoDetails;

    @BeforeEach
    public void setUp() {
        cargoDetails = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CargoDetail detail = new CargoDetail();
            detail.setName("Item" + i);
            cargoDetails.add(detail);
        }
    }
        @Test
    public void testToDto_WithValidData_ShouldConvertCorrectly() {
        List<CargoDetailsDTO> cargoDetailsDTOS = CargoDetail.toDto(cargoDetails);

        assertEquals(10, cargoDetailsDTOS.size());
        for (int i = 0; i < 10; i++) {
            assertEquals("Item" + i, cargoDetailsDTOS.get(i).getName());
        }
    }

      @Test
    public void testToDto_WithEmptyList_ShouldReturnEmptyList() {
        List<CargoDetail> emptyList = new ArrayList<>();
        List<CargoDetailsDTO> cargoDetailsDTOS = CargoDetail.toDto(emptyList);

        assertEquals(0, cargoDetailsDTOS.size());
    }


}
