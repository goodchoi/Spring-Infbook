package infbook.infbook.domain.item.repository;

import infbook.infbook.abstractTest.RepostoryTest;
import infbook.infbook.domain.category.domain.SubCategoryItem;
import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.item.dto.ItemHomeDto;
import infbook.infbook.domain.item.dto.ItemListDto;
import infbook.infbook.domain.order.domain.OrderItem;
import infbook.infbook.utils.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static infbook.infbook.utils.models.*;

@Slf4j
class ItemRepositoryTest extends RepostoryTest {

    @Test
    @DisplayName("하위 카테고리이름으로 검색 - default 페이지 item 1개 0페이지,페이지사이즈 10 검증")
    void searchDefaultItemBySubcategoryName() {

        Page<ItemListDto> itemPages = itemRepository.searchAllBySubcategoryName(SUBCATEGORY_ENG_NAME
                , PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate")));

        List<ItemListDto> content = itemPages.getContent();
        int totalPages = itemPages.getTotalPages();

        assertThat(totalPages).isEqualTo(1);
        assertThat(content).hasSize(1);
        assertThat(content).extracting("name").containsOnly(ITEM_NAME);

    }

    @Test
    @DisplayName(" 하위 카테고리이름으로 검색 - default 페이지 item 40개 존재시 검증 - 동적 sorting 동작확인(필드명,오름차순,내림차순)" +
            "총 페이지 갯수 40, 페이지사이즈 10,총 로우수 40개,내림차순 정렬 검증")
    void searchItemBySubcategoryName() {
        List<Item> testItems = itemMuiltipleInsert(); //총 item 40개 ,

        Page<ItemListDto> itemPages = itemRepository.searchAllByCategoryName(CATEGORY_ENG_NAME
                , PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate")));

        List<ItemListDto> content = itemPages.getContent();
        int totalPages = itemPages.getTotalPages();

        assertThat(totalPages).isEqualTo(4);
        assertThat(content).hasSize(10);
        assertThat(itemPages.getTotalElements()).isEqualTo(40L);
        assertThat(content).extracting("publicationDate").isSortedAccordingTo(Collections.reverseOrder());

        content.forEach(e -> log.info("{}", e));
    }

    @Test
    @DisplayName("[판매순]하위 카테고리이름으로 검색 - default 페이지 item 40개 존재시 검증 " +
            "총 페이지 갯수 40, 페이지사이즈 10,총 로우수 40개,판매량 높은 순서부터 내림차순 검증")
    void searchPopularBySubcategoryName() {


        List<Item> testItems = itemMuiltipleInsert(); //총 item 40개 ,
        //테스트전 주문 먼저 생성.
        //order는 이테스트에서 아무 영향이 없으므로 기존에 존재하는 Order 선택

        OrderItem order1 = OrderItem.builder().order(savedOrder)
                .item(testItems.get(testItems.size() - 1))
                .quantity(13)
                .build();
        OrderItem order2 = OrderItem.builder().order(savedOrder)
                .item(testItems.get(testItems.size() - 2))
                .quantity(12)
                .build();
        OrderItem order3 = OrderItem.builder().order(savedOrder)
                .item(testItems.get(testItems.size() - 3))
                .quantity(11)
                .build();

        List<OrderItem> soldItems = List.of(order1, order2, order3);
        orderItemRepository.saveAll(soldItems);


        Page<ItemListDto> itemPages = itemRepository.searchPopularBySubCategoryName(SUBCATEGORY_ENG_NAME
                , PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "popular")));

        List<ItemListDto> content = itemPages.getContent();
        int totalPages = itemPages.getTotalPages();
        content.forEach(e -> log.info("{}", e));

        assertThat(totalPages).isEqualTo(4);
        assertThat(content).hasSize(10);
        assertThat(itemPages.getTotalElements()).isEqualTo(40L);
        assertThat(content.subList(0,3)).extracting("id")
                .isEqualTo((soldItems.stream().map(oi -> oi.getItem().getId()).toList()));

    }

    @Test
    @DisplayName("판매순으로 베스트셀러 5개의 목록을 찾을수있다.")
    void findBestSellers() {


        List<Item> testItems = itemMuiltipleInsert(); //총 item 40개 ,
        //테스트전 주문 먼저 생성.
        //order는 이테스트에서 아무 영향이 없으므로 기존에 존재하는 Order 선택

        OrderItem order1 = OrderItem.builder().order(savedOrder)
                .item(testItems.get(testItems.size() - 1))
                .quantity(13)
                .build();
        OrderItem order2 = OrderItem.builder().order(savedOrder)
                .item(testItems.get(testItems.size() - 2))
                .quantity(12)
                .build();
        OrderItem order3 = OrderItem.builder().order(savedOrder)
                .item(testItems.get(testItems.size() - 3))
                .quantity(11)
                .build();

        List<OrderItem> soldItems = List.of(order1, order2, order3);
        orderItemRepository.saveAll(soldItems);

        List<ItemHomeDto> bestSellers = itemRepository.findBestSellers();

        bestSellers.forEach(e -> log.info("{}", e));

        assertThat(bestSellers.subList(0,3)).extracting("id")
                .isEqualTo((soldItems.stream().map(oi -> oi.getItem().getId()).toList()));

    }

    @Test
    @DisplayName("카테고리이름으로 검색 - default 페이지 item 1개존재시 0페이지,결과 로우수 1 검증")
    void searchDefaultItemByCategoryName() {

        Page<ItemListDto> itemPages = itemRepository.searchAllByCategoryName(CATEGORY_ENG_NAME
                , PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate")));

        List<ItemListDto> content = itemPages.getContent();
        int totalPages = itemPages.getTotalPages();

        assertThat(totalPages).isEqualTo(1);
        assertThat(content).hasSize(1);
        assertThat(content).extracting("name").containsOnly(ITEM_NAME);
    }

    @Test
    @DisplayName("하위카테고리이름으로 검색 - default 페이지 item 1개존재시 0페이지,결과 로우수 1 검증")
    void searchItemBySubCategoryName() {
        List<Item> testItems = itemMuiltipleInsert(); //총 item 40개 ,

        Page<ItemListDto> itemPages = itemRepository.searchAllBySubcategoryName(SUBCATEGORY_ENG_NAME
                , PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate")));

        List<ItemListDto> content = itemPages.getContent();
        int totalPages = itemPages.getTotalPages();

        assertThat(totalPages).isEqualTo(4);
        assertThat(content).hasSize(10);
        assertThat(itemPages.getTotalElements()).isEqualTo(40L);
        assertThat(content).extracting("publicationDate").isSortedAccordingTo(Collections.reverseOrder());
        assertThat(itemPages.getTotalPages()).isEqualTo(4);

        content.forEach(e -> log.info("{}", e));
    }

    private List<Item> itemMuiltipleInsert() {
        List<Item> testItems = new ArrayList<>();
        List<SubCategoryItem> testSubcategoryItems = new ArrayList<>();

        for (int i = 0; i < 39; i++) {

            Item item = Item.builder()
                    .name("테스트 도서명" + i + "번째")
                    .publicationDate(LocalDate.now().minusDays((long) i))
                    .category(savedCategory)
                    .stockQuantity(100)
                    .fileName("9788960777330.jpeg")
                    .author("최동현")
                    .publisher("인프북")
                    .price(10000 + i)
                    .index("목차")
                    .isbn("isbn")
                    .pageNumber(100+i)
                    .build();
            testItems.add(item);
            testSubcategoryItems.add(SubCategoryItem.builder().subCategory(savedSubCategory)
                    .item(item).build());
        }
        itemAdminRepository.saveAll(testItems);
        subCategoryItemRepository.saveAll(testSubcategoryItems);

        return testItems;
    }


}