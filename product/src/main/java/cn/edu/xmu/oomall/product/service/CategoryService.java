//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.BloomFilter;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.dao.CategoryDao;
import cn.edu.xmu.oomall.product.dao.ProductDao;
import cn.edu.xmu.oomall.product.dao.bo.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Slf4j
public class CategoryService {


    private final ProductDao productDao;
    private final CategoryDao categoryDao;
    private final RedisUtil redisUtil;

    /**
     * 获取某个商品类目的所有子类目
     *
     * @param id 商品类目Id
     * @return 子类目列表
     */
    public List<Category> retrieveSubCategories(Long id) {
        log.debug("getSubCategories: id = {}", id);
        String key = BloomFilter.PRETECT_FILTERS.get("CategoryPid");
        if (redisUtil.bfExist(key, id)) {
            return new ArrayList<>();
        }
        Category category=categoryDao.findById(id);
        List<Category> categories = category.getSubCategoryList(id);
        if (categories.isEmpty()) {
            redisUtil.bfAdd(key, id);
        }
        return categories;
    }

    /**
     * 创建商品类目
     * @param id 父类目id，若为0则为顶级类目
     * @param category 类目信息
     * @param creator 创建者
     * @return 创建的类目
     * @modify huangzian 将职责转给父对象
     * 2023-dgn2-004
     */
    public Category createSubCategory(Long id, Category category, UserDto creator) {
        Category parent = this.categoryDao.findById(id);
        return parent.createSubCategory(category, creator);
    }

    /**
     * 修改商品类目
     * @param category 类目信息
     * @param modifier 修改者
     */
    public void updateCategory(Category category, UserDto modifier) {
        String key = this.categoryDao.save(category, modifier);
        redisUtil.del(key);
    }

    /**
     * 删除分类
     * @param id 分类id
     * @param userDto 操作者
     */
    public void deleteCategory(Long id, UserDto userDto) {
        if (!Category.PARENTID.equals(id)) {
            List<String> keyList = new ArrayList<>();
            Category category = this.categoryDao.findById(id);
            List<Category> subCategories = category.getChildren();
            for (Category sub : subCategories) {
                try {
                    keyList.add(this.categoryDao.delete(sub.getId()));
                    keyList.addAll(this.productDao.changeToNoCategoryProduct(sub.getId(), userDto));
                }catch (BusinessException e){
                    if (ReturnNo.RESOURCE_ID_NOTEXIST == e.getErrno()){
                        log.error("deleteCategory: subcatory id = {} not exist.", sub.getId());
                    }
                }
            }
            keyList.add(this.categoryDao.delete(id));
            redisUtil.del(keyList.toArray(new String[keyList.size()]));
        }else{
            log.error("deleteCategory: the root category can not be deleted.");
        }
    }
}
