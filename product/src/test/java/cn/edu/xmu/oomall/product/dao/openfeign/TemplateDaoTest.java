package cn.edu.xmu.oomall.product.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Template;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;


@SpringBootTest(classes = ProductTestApplication.class)
@Slf4j
public class TemplateDaoTest {

    @Autowired
    private TemplateDao templateDao;

    @SpyBean
    private ShopMapper shopMapper; // 模拟 ShopMapper

    @Test
    public void testFindByIdSuccess() {
        // 创建一个 InternalReturnObject，模拟成功的返回
        InternalReturnObject<Template> mockReturnObject = new InternalReturnObject<>();
        mockReturnObject.setErrno(ReturnNo.OK.getErrNo()); // 设置成功的 errno
        mockReturnObject.setData(new Template(1L, "Template1"));

        // 配置 mock 对象的行为
        doReturn(mockReturnObject).when(shopMapper).getTemplateById(1L, 1L);

        // 测试 TemplateDao 调用
        Template template = templateDao.findById(1L, 1L);

        // 验证返回值
        assertNotNull(template);
        assertEquals(1L, template.getId());
        log.debug("Test successful: {}", template);
    }

    @Test
    public void testFindByIdResourceNotExist() {
        // 创建一个 InternalReturnObject，模拟资源不存在的情况
        InternalReturnObject<Template> mockReturnObject = new InternalReturnObject<>();
        mockReturnObject.setErrno(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()); // 设置不存在的 errno
        mockReturnObject.setErrmsg("Template not found");

        // 配置 mock 对象的行为
        doReturn(mockReturnObject).when(shopMapper).getTemplateById(1L, 9999L);

        // 测试 TemplateDao 调用，期望抛出 BusinessException
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            templateDao.findById(1L, 9999L);
        });

        // 验证异常内容
        assertEquals(ReturnNo.RESOURCE_ID_NOTEXIST, exception.getErrno());
        log.debug("Exception captured: {}", exception.getMessage());
    }

    @Test
    public void testFindByIdOutScope() {
        // 创建一个 InternalReturnObject，模拟超出范围的情况
        InternalReturnObject<Template> mockReturnObject = new InternalReturnObject<>();
        mockReturnObject.setErrno(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()); // 设置超出范围的 errno
        mockReturnObject.setErrmsg("Template is out of scope");

        // 配置 mock 对象的行为
        doReturn(mockReturnObject).when(shopMapper).getTemplateById(1L, 16L);

        // 测试 TemplateDao 调用，期望抛出 BusinessException
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            templateDao.findById(1L, 16L);
        });

        // 验证异常内容
        assertEquals(ReturnNo.RESOURCE_ID_OUTSCOPE, exception.getErrno());
        log.debug("Exception captured: {}", exception.getMessage());
    }
}
