package cn.edu.xmu.oomall.sfexpress.dao.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@Data
@NoArgsConstructor
public class RouteResps {

    @JsonProperty("mailNo")
    private String mailNo;
    @JsonProperty("routes")
    private List<Route> routes;

    public static List<Route> generateRandomRouteList(int numberOfRoutes) {
        List<Route> routeList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfRoutes; i++) {
            Route route = new Route();
            route.setAcceptTime(Timestamp.valueOf("2023-12-11 " + random.nextInt(24) + ":" + random.nextInt(60) + ":" + random.nextInt(60)));
            route.setAcceptAddress(generateRandomCity());
            route.setOpCode(String.valueOf(random.nextInt(100)));
            route.setRemark(generateRandomRemark());
            routeList.add(route);
        }

        return routeList;
    }

    private static String generateRandomCity() {
        String[] cities = {"深圳", "上海", "北京", "广州", "成都", "重庆", "武汉", "杭州", "南京", "西安"};
        Random random = new Random();
        return cities[random.nextInt(cities.length)];
    }

    private static String generateRandomRemark() {
        String[] remarks = {"已揽收", "运输中", "派件中", "已签收", "异常件"};
        Random random = new Random();
        return remarks[random.nextInt(remarks.length)];
    }

    public static String convertRouteListToJson(RouteResps routeResps) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(routeResps);
    }
}

