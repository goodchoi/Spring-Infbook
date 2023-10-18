package infbook.infbook.domain.item.controller;

import infbook.infbook.abstractTest.ControllerTest;
import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategory;
import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.item.dto.ItemListDto;
import infbook.infbook.domain.item.dto.ItemSingleDto;
import infbook.infbook.global.util.CategoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
class ItemControllerTest extends ControllerTest {

    private static MockedStatic<CategoryUtil> mockedStatic;

    private Category category;
    private SubCategory subCategory;
    private Item savedItem;

    @BeforeEach
    void dataSetUp() {
        category = categoryRepository.save(Category.builder()
                .name("백엔드")
                .engName("backend")
                .build());

        subCategory = subCategoryRepository.save(SubCategory.builder()
                .name("스프링")
                .engName("spring")
                .parent(category)
                .build());

        savedItem = Item.builder()
                .name("테스트 도서명번째")
                .publicationDate(LocalDate.now())
                .category(category)
                .subTitle("책소개소책소개ㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐ")
                .stockQuantity(10)
                .fileName("9788960777330.jpeg")
                .author("최동현")
                .publisher("인프북")
                .price(10000)
                .index("목차")
                .isbn("isbn")
                .pageNumber(100)
                .build();

        itemAdminRepository.save(savedItem);
    }

    @BeforeAll
    static void setMockedStatic() {
        mockedStatic = Mockito.mockStatic(CategoryUtil.class);
    }

    @AfterAll
    static void closeMockedStatic() {
        mockedStatic.close();
    }

    @DisplayName("카테고리 별 상품 조회 - 조회하지않는 카테고리로 조회 시 404에러 발생")
    @Test
    void getItemPageByCategory_404() throws Exception {
        String nonExistCategory = "None";
        BDDMockito.given(CategoryUtil.hasCategory(any(String.class))).willReturn(false);


        if (!CategoryUtil.hasCategory(nonExistCategory)) {
            mockMvc.perform(get("/item/category/" + nonExistCategory))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("카테고리 별 상품 조회 - 조회하지않는 하위 카테고리로 조회 시 404에러 발생")
    @Test
    void getItemPageBySubCategory_404() throws Exception {
        String nonExistCategory = "None";
        BDDMockito.given(CategoryUtil.hasCategory(any(String.class))).willReturn(true);
        BDDMockito.given(CategoryUtil.hasSubCategory(any(String.class))).willReturn(false);


        if (!CategoryUtil.hasCategory(nonExistCategory)) {
            mockMvc.perform(get("/item/category/inputCategory/inputSubCategory"))
                    .andExpect(status().isNotFound());
        }
    }


    @DisplayName("카테고리 별 상품 조회 - 존재하는 카테고리로 상품조회시 아무 파라미터를 넘기지않으면 최신순,5개보기로 상품리스트 조회가능")
    @Test
    void getItemPageByCategory_default() throws Exception {

        BDDMockito.given(CategoryUtil.hasCategory(any(String.class))).willReturn(true);

        mockMvc.perform(get("/item/category/" + category.getEngName()))
                .andExpect(result ->
                {
                    PageImpl<ItemListDto> page = (PageImpl<ItemListDto>) result.getModelAndView().getModel().get("page");
                    assertThat(page.getSize()).isEqualTo(5);
                    assertThat(page.getSort().descending()).isEqualTo(Sort.by(Sort.Direction.DESC,"publicationDate"));
                    assertThat(page.getNumberOfElements()).isEqualTo(1);
                });
    }

    @DisplayName("카테고리 별 상품 조회 - 존재하는 카테고리로 상품조회시 정렬기준, 페이지사이즈를 입력받아서 조회할 수 있음.")
    @Test
    void getItemPageByCategory_parameter() throws Exception {

        BDDMockito.given(CategoryUtil.hasCategory(any(String.class))).willReturn(true);

        mockMvc.perform(get("/item/category/" + category.getEngName() + "?sort=highprice&size=10&page=0"))
                .andExpect(result ->
                {
                    PageImpl<ItemListDto> page = (PageImpl<ItemListDto>) result.getModelAndView().getModel().get("page");
                    assertThat(page.getSize()).isEqualTo(10);
                    assertThat(page.getSort().descending()).isEqualTo(Sort.by(Sort.Direction.DESC,"price"));
                    assertThat(page.getNumberOfElements()).isEqualTo(1);
                });
    }

    @DisplayName("상품 상세 조회 - 존재하지 않는 상품으로 조회시 500에러 발생")
    @Test
    void getItem_fail() throws Exception {

        BDDMockito.given(CategoryUtil.hasCategory(any(String.class))).willReturn(true);

        mockMvc.perform(get("/item/" + "2"))
                .andExpect(status().isNotFound());

    }

    @DisplayName("상품 상세 조회 - 성공")
    @Test
    void getItem_success() throws Exception {

        BDDMockito.given(CategoryUtil.hasCategory(any(String.class))).willReturn(true);

        Long existinItemId = savedItem.getId();

        mockMvc.perform(get("/item/" + existinItemId))
                .andExpect(result ->
                {
                    ItemSingleDto item = (ItemSingleDto) result.getModelAndView().getModel().get("item");
                    assertThat(item.getId()).isEqualTo(existinItemId);
                })
                .andExpect(status().isOk());
    }

    @DisplayName("상품 검색 - 성공")
    @Test
    void search_item_success() throws Exception {

        Long existinItemId = savedItem.getId();

        mockMvc.perform(get("/item/search")
                        .param("keyword",savedItem.getName()))
                .andExpect(result ->
                {
                    PageImpl<ItemListDto> page = (PageImpl<ItemListDto>) result.getModelAndView().getModel().get("page");
                    assertThat(page.getNumberOfElements()).isEqualTo(1);
                })
                .andExpect(status().isOk());
    }

}