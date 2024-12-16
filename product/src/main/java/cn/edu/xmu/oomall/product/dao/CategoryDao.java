//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.dao.bo.Category;
import cn.edu.xmu.oomall.product.mapper.jpa.CategoryPoMapper;
import cn.edu.xmu.oomall.product.mapper.po.CategoryPo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.IDNOTEXIST;
import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;

@Repository
@RefreshScope
@RequiredArgsConstructor
public class CategoryDao {

    private final static String KEY = "C%d";

    @Value("${oomall.category.timeout}")
    private int timeout;

    private final CategoryPoMapper categoryPoMapper;
    private final RedisUtil redisUtil;

    private Category build(CategoryPo po, Optional<String> redisKey) {
        Category bo = CloneFactory.copy(new Category(), po);
        this.build(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private Category build(Category bo) {
        bo.setCategoryDao(this);
        return bo;
    }

    /**
     * 按照id查找分类对象
     *
     * @param id id
     * @return 分类对象
     * @throws RuntimeException
     */
    public Category findById(Long id) throws RuntimeException {
        String key = String.format(KEY, id);
        Category bo = (Category) redisUtil.get(key);
        if (bo != null) {
            build(bo);
            return bo;
        }

        Optional<CategoryPo> ret = this.categoryPoMapper.findById(id);
        if (ret.isPresent()) {
            return this.build(ret.get(), Optional.of(key));
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商品类目", id));
        }
    }

    /**
     * 查询下级分类
     *
     * @param id 分类id
     * @return 下级分类
     */
    public List<Category> retrieveSubCategory(Long id) throws RuntimeException {
        List<CategoryPo> poList = categoryPoMapper.findByPidEquals(id, PageRequest.of(0, MAX_RETURN));
        return poList.stream().map(po -> this.build(po, Optional.of(String.format(KEY, po.getId())))).collect(Collectors.toList());
    }

    /**
     * 创建类目
     *
     * @param bo      新对象信息
     * @param userDto 操作者
     * @return 创建的新对象
     */
    public Category insert(Category bo, UserDto userDto) throws RuntimeException {
        bo.setGmtCreate(LocalDateTime.now());
        bo.setCreator(userDto);
        CategoryPo po = CloneFactory.copy(new CategoryPo(), bo);
        this.categoryPoMapper.save(po);
        //TODO: 名称重复
        bo.setId(po.getId());
        return bo;
    }

    /**
     * 根据Category.Id更新类目
     *
     * @param bo      更新的信息
     * @param userDto 操作者
     * @return 需要删除的redis key
     */
    public String save(Category bo, UserDto userDto) throws RuntimeException {
        bo.setGmtModified(LocalDateTime.now());
        bo.setModifier(userDto);
        CategoryPo po = CloneFactory.copy(new CategoryPo(), bo);
        CategoryPo ret = this.categoryPoMapper.save(po);
        if (IDNOTEXIST.equals(ret.getId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商品类目", bo.getId()));
        }
        return String.format(KEY, bo.getId());
    }

    /**
     * 根据Category.Id删除类目
     *
     * @param id category id
     * @return 需要删除的redis key
     */
    public String delete(Long id) throws RuntimeException {

        if (!this.categoryPoMapper.existsById(id))
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商品类目", id));
        this.categoryPoMapper.deleteById(id);
        return String.format(KEY, id);
    }
}
