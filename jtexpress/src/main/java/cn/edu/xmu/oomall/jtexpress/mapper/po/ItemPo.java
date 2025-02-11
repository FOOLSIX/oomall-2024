package cn.edu.xmu.oomall.jtexpress.mapper.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "jtexpress_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemPo {

    /*
     * 主键 ID，自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tx_logistic_id", length = 30)
    private String txLogisticId;

    /*
     * 物品类型：
     * bm000001 文件
     * bm000002 数码产品
     * bm000003 生活用品
     * bm000004 食品
     * bm000005 服饰
     * bm000006 其他
     * bm000007 生鲜类
     * bm000008 易碎品
     * bm000009 液体
     */
    @Column(name = "item_type", length = 30)
    private String itemType;

    /*
     * 物品名称
     */
    @Column(name = "item_name", length = 30)
    private String itemName;

    /*
     * 物品中文名称
     */
    @Column(name = "chinese_name", length = 60)
    private String chineseName;

    /*
     * 物品英文名称
     */
    @Column(name = "english_name", length = 60)
    private String englishName;

    /*
     * 件数，≤1
     */
    @Column(name = "number")
    private int number;

    /*
     * 申报价值(数值型)
     */
    @Column(name = "item_value", length = 20)
    private String itemValue;

    /*
     * 申报货款币别（默认本国币别，如：RMB）
     */
    @Column(name = "price_currency", length = 20)
    private String priceCurrency;

    /*
     * 物品描述
     */
    @Column(name = "desc", length = 100)
    private String desc;

    /*
     * 商品URL
     */
    @Column(name = "item_url", length = 100)
    private String itemUrl;

}

