//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.region.controller.dto;

import cn.edu.xmu.javaee.core.validation.NewGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * 地区数据传输对象，用于从前端接收数据
 */
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDto {
    @NotBlank(message = "地区名不能为空", groups = {NewGroup.class})
    private String name;
    @NotBlank(message = "地区简称不能为空", groups = {NewGroup.class})
    private String shortName;
    @NotBlank(message = "地区全称不能为空", groups = {NewGroup.class})
    private String mergerName;
    @NotBlank(message = "地区拼音不能为空", groups = {NewGroup.class})
    private String pinyin;
    @NotNull(message = "经度不能为空", groups = {NewGroup.class})
    private Double lng;
    @NotNull(message = "纬度不能为空", groups = {NewGroup.class})
    private Double lat;
    @NotBlank(message = "地区码不能为空", groups = {NewGroup.class})
    private String areaCode;
    @NotBlank(message = "邮政编码不能为空", groups = {NewGroup.class})
    private String zipCode;
    @NotBlank(message = "电话区号不能为空", groups = {NewGroup.class})
    private String cityCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMergerName() {
        return mergerName;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
