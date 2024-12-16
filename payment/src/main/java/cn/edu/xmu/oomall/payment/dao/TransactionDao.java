package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.dao.bo.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Slf4j
public abstract class TransactionDao {

    /**
     * 更新对象
     * @author Ming Qiu
     * <p>
     * date: 2022-11-13 20:46
     * @param trans 更新的值， id不能为null
     * @param userDto
     * @throws RuntimeException
     */
    public <T extends Transaction> void save(T trans, UserDto userDto){
        if (trans.getId().equals(null)){
            throw new IllegalArgumentException("TransactionDao.save: trans's id is null");
        }

        trans.setModifier(userDto);
        trans.setGmtModified(LocalDateTime.now());
        if (0 == this.save(trans)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), trans.getTransName(), trans.getId()));
        }
    }

    /**
     * 插入对象
     *
     * @param obj 对象的值
     * @param user 操作用户
     * @throws RuntimeException
     * @author Ming Qiu
     * <p>
     * date: 2022-11-12 20:05
     */
    public <T extends Transaction>  T insert(T obj, UserDto user){
        log.debug("insert: obj = {}", obj);
        obj.setCreator(user);
        obj.setGmtCreate(LocalDateTime.now());
        obj.setId(null);
        Transaction newObj = this.insert(obj);
        return (T) newObj;
    }
    protected abstract Integer save(Transaction obj);

    protected abstract Transaction insert(Transaction obj);

    public abstract Transaction findById(Long shopId, Long id);
}


