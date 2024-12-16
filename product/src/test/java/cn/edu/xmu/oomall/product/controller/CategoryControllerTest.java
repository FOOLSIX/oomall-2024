package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.bo.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
/**
 * @author huangzian
 * 2023-dgn2-004
 */
@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RedisUtil redisUtil;
    private static String adminToken;
    private static String shopToken;
    private static final String STATES="/products/states";
    private static final String SUB_CATEGORIES = "/categories/{id}/subcategories";
    private static final String ADMIN_SUB_CATEGORIES = "/platforms/{shopId}/categories/{id}/subcategories";
    private static final String CREATE_CATEGORY = "/platforms/{shopId}/categories/{id}/subcategories";
    private static final String UPDATE_CATEGORY = "/platforms/{shopId}/categories/{id}";
    private static final String DELETE_CATEGORY = "/platforms/{shopId}/categories/{id}";
    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 0, 3600);
        shopToken=jwtHelper.createToken(1L,"shop1",1L,1,3600);
    }

    /**
     * 获得商品的所有状态
     * @author huangzian
     * 2023-dgn2-004
     * @throws Exception
     */
    @Test
    public void getStatesTest() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get(STATES)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     * 查询商品分类关系(无需token) redis无效
     * @author huangzian
     * 2023-dgn2-004
     */
    @Test
    public void getSubCategoriesTestTestGivenId1L() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(SUB_CATEGORIES, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", is(7)));
    }
    /**
     * 查询商品分类关系(无需token) 本身是二级分类
     * @author huangzian
     * 2023-dgn2-004
     */
    @Test
    public void getSubCategoriesTestTestGivenId186L() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(SUB_CATEGORIES, 186L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", is(0)));
    }
    /**
     * 查询商品分类关系(无需token) redis有效
     * @author huangzian
     * 2023-dgn2-004
     */
    @Test
    public void getSubCategoriesTestTestGivenId1LAndRedisHasKey() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(true);
        Category category= Category.builder().id(1L).build();
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(category);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SUB_CATEGORIES, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", is(7)));
    }
    /**
     * 查询商品分类关系(无需token) redisBF有效
     * @author huangzian
     * 2023-dgn2-004
     */
    @Test
    public void getSubCategoriesTestTestGivenId1LAndRedisBFExist() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(true);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        Category category= Category.builder().id(1L).build();
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(category);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SUB_CATEGORIES, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", is(0)));
    }
    @Test
    public void getSubCategoriesTestGivenId20L() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(SUB_CATEGORIES, 20L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    /**
     * 管理员新增商品类目
     * @throws Exception
     */
    @Test
    public void createSubCategoriesTest() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String json = "{\"name\":\"test\", \"commissionRatio\": 50}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_CATEGORY, 0L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.CREATED.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("test"));
    }

    @Test
    public void testCreateSubCategoriesGivenCommissionRationNegative() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String json = "{\"name\":\"test\", \"commissionRatio\": -1}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_CATEGORY, 0L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateSubCategoriesWhenIdIsNotFirstCategory() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String json = "{\"name\":\"test\", \"commissionRatio\": 4}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_CATEGORY, 0L, 200L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.CATEGORY_NOTPERMIT.getErrNo()));
    }

    @Test
    public void testCreateSubCategoriesGivenCommissionRationIs100() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String json = "{\"name\":\"test\", \"commissionRatio\": 100}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_CATEGORY, 0L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.CREATED.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("test"));
    }

    @Test
    public void testCreateSubCategoriesGivenJsonEmpty() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String json = "{}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_CATEGORY, 0L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateSubCategoriesWhenIsNotAdmin() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String json = "{\"name\":\"test\", \"commissionRatio\": 100}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_CATEGORY, 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("authorization", shopToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.AUTH_NO_RIGHT.getErrNo()));
    }

    @Test
    public void testCreateSubCategoriesPIdNotExist() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String json = "{\"pid\":114514, \"name\":\"test\", \"commissionRatio\": 100}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_CATEGORY, 0L, 114514L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    /**
     * 管理员查询商品分类关系
     * @throws Exception
     */
    @Test
    public void testAdminGetSubCategory() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_SUB_CATEGORIES, 0L,1L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == '186')].name", hasItem("女式上装")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == '186')].commissionRatio", hasItem(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == '189')].gmtCreate", hasItem("2021-11-11T12:21:18")));
    }
    @Test
    public void testGetAdminSubCategoriesGivenId20L() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_SUB_CATEGORIES, 0L,20L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }
    @Test
    public void testAdminGetSubCategoriesWhenIsNotAdmin() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_SUB_CATEGORIES, 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", shopToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.AUTH_NO_RIGHT.getErrNo()));
    }
    /**
     * 管理员更新商品分类关系
     * @throws Exception
     */
    @Test
    public void updateCategoriesTest() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String json = "{\"name\":\"test\", \"commissionRatio\": 100}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_CATEGORY, 0L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0));
    }

    @Test
    public void testUpdateCategoriesWhenIdNotExist() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String json = "{\"name\":\"test\", \"commissionRatio\": 100}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_CATEGORY, 0L, 500L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(4));
    }

    @Test
    public void testAdminUpdateCategoriesWhenIsNotAdmin() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String json = "{\"name\":\"test\", \"commissionRatio\": 100}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_CATEGORY, 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", shopToken)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.AUTH_NO_RIGHT.getErrNo()));
    }


    /**
     * 管理员删除商品分类关系
     * @throws Exception
     */
    @Test
    public void deleteCategoryTest() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CATEGORY, 0L, 313L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0));

    }

    @Test
    public void testDeleteCategoryWhenIsNotAdmin() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CATEGORY, 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", shopToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.AUTH_NO_RIGHT.getErrNo()));
    }

    @Test
    public void testDeleteCategoryWhenIdNotExist() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CATEGORY, 0L, 114514L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }
    @Test
    public void testDeleteCategoryWhenIdIs1L() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CATEGORY, 0L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0));
    }

    @Test
    public void testDeleteCategoryWhenIdIs0L() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CATEGORY, 0L, 0L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0));
    }

}
