//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.javaee.core.model.bo;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * OOMall的通用对象
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Data
public abstract class OOMallObject implements Cloneable{

    protected Long id;
    /**
     * 创建者id
     */
    protected Long creatorId;

    /**
     * 创建者
     */
    protected String creatorName;

    /**
     * 修改者id
     */
    protected Long modifierId;

    /**
     * 修改者
     */
    protected String modifierName;

    /**
     * 创建时间
     */
    protected LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    protected LocalDateTime gmtModified;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public OOMallObject(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified) {
        this.id = id;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.modifierId = modifierId;
        this.modifierName = modifierName;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public  void setCreator(UserDto user){
        this.creatorId = user.getId();
        this.creatorName  = user.getName();
    }

    public  void setModifier(UserDto user){
        this.modifierId = user.getId();
        this.modifierName  = user.getName();
    }

    public abstract void setGmtCreate(LocalDateTime gmtCreate);

    public abstract void setGmtModified(LocalDateTime gmtModified);

}
