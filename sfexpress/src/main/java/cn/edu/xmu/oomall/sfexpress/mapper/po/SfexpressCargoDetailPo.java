package cn.edu.xmu.oomall.sfexpress.mapper.po;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@Entity
@Table(name = "sfexpress_cargo_detail", schema = "sfexpress", catalog = "")
public class SfexpressCargoDetailPo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "express_id")
    private long expressId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExpressId() {
        return expressId;
    }

    public void setExpressId(long expressId) {
        this.expressId = expressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SfexpressCargoDetailPo that = (SfexpressCargoDetailPo) o;
        return id == that.id && expressId == that.expressId && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, expressId);
    }
}
