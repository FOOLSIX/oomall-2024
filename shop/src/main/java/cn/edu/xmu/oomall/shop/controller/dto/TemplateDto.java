package cn.edu.xmu.oomall.shop.controller.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TemplateDto {

    public static Integer WEIGHT = 0;
    public static Integer PIECE = 1;
    private String name;
    private Byte defaultModel;
    private Integer type;
    private String divideStrategy;
    private String packAlgorithm;

    public void setPackAlgorithm(String packAlgorithm) {
        this.packAlgorithm = packAlgorithm;
    }

    public void setDivideStrategy(String divideStrategy) {
        this.divideStrategy = divideStrategy;
    }

    public String getPackAlgorithm() {
        return packAlgorithm;
    }

    public String getDivideStrategy() {
        return divideStrategy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getDefaultModel() {
        return defaultModel;
    }

    public void setDefaultModel(Byte defaultModel) {
        this.defaultModel = defaultModel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
