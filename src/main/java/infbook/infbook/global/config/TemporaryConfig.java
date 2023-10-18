package infbook.infbook.global.config;

import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategory;
import infbook.infbook.domain.category.domain.SubCategoryItem;
import infbook.infbook.domain.category.repository.CategoryRepository;
import infbook.infbook.domain.category.repository.SubCategoryItemRepository;
import infbook.infbook.domain.category.repository.SubCategoryRepository;
import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.item.repository.ItemAdminRepository;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.domain.UserLevel;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.domain.model.Address;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.repository.ShoppingCartRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class TemporaryConfig {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BCryptPasswordEncoder encoder;
    private final ItemAdminRepository itemAdminRepository;
    private final SubCategoryItemRepository subCategoryItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    private Member getMEMBER() {
        return Member.builder()
                .name(MEMBER_NAME)
                .accountId(MEMBER_ACCOUNTID)
                .password(encoder.encode(MEMBER_PASSWORD))
                .birthDate(MEMBER_BIRTHDATE)
                .email(MEMBER_EMAIL)
                .address(Address.builder().zipcode("123-33").street("test").city("test").detailedAddress("test").build())
                .telephone(MEMBER_TELEPHONE)
                .userLevel(MEMBER_USERLEVEL)
                .build();
    }

    public static final Long MEMBER_ID = 1L;
    public static final String MEMBER_NAME = "테스트멤버이름";
    public static final String MEMBER_ACCOUNTID = "test";
    public static final String MEMBER_PASSWORD = "test1233!";
    public static final String MEMBER_EMAIL = "jangu3384@naver.com";
    public static final LocalDate MEMBER_BIRTHDATE = LocalDate.of(1995, 2, 22);
    public static final String MEMBER_TELEPHONE = "010-0000-000";
    public static final UserLevel MEMBER_USERLEVEL = UserLevel.ROLE_USER;

    @PostConstruct()
    private void addMemeber() {
        Member savedMember = memberRepository.save(getMEMBER());
        shoppingCartRepository.save(ShoppingCart.builder().member(savedMember)
                .build());


        Category backend = Category.builder()
                .name("백엔드")
                .engName("backend")
                .build();
        Category infra = Category.builder()
                .name("인프라")
                .engName("infra")
                .build();

        Category frontEnd = Category.builder()
                .name("프론트")
                .engName("front")
                .build();

        categoryRepository.saveAll(List.of(backend, infra
                , frontEnd));


        SubCategory java = subCategoryRepository.save(SubCategory.builder()
                .name("자바")
                .parent(backend)
                .engName("java")
                .build());
        subCategoryRepository.save(SubCategory.builder()
                .name("스프링")
                .engName("spring")
                .parent(backend)
                .build());
        subCategoryRepository.save(SubCategory.builder()
                .name("리액트")
                .engName("react")
                .parent(frontEnd)
                .build());
        subCategoryRepository.save(SubCategory.builder()
                .name("몰라")
                .engName("nm")
                .parent(frontEnd)
                .build());
        subCategoryRepository.save(SubCategory.builder()
                .name("도커")
                .engName("docker")
                .parent(infra)
                .build());
        subCategoryRepository.save(SubCategory.builder()
                .name("몰랑")
                .engName("aa")
                .parent(infra)
                .build());

        itemMuiltipleInsert(backend, java);
    }


    private List<Item> itemMuiltipleInsert(Category category, SubCategory subCategory) {
        List<Item> testItems = new ArrayList<>();
        List<SubCategoryItem> testSubcategoryItems = new ArrayList<>();

        int stock = 10;
        for (int i = 0; i < 112; i++) {

            Item item = Item.builder()
                    .name("테스트 도서명" + i + "번째")
                    .publicationDate(LocalDate.now().minusDays((long) i))
                    .category(category)
                    .subTitle("책소개소책소개ㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐ")
                    .stockQuantity(10)
                    .fileName("9788960777330.jpeg")
                    .author("최동현")
                    .publisher("인프북")
                    .price(10000 + i)
                    .index("목차")
                    .isbn("isbn")
                    .pageNumber(100+i)
                    .build();
            testItems.add(item);
            testSubcategoryItems.add(SubCategoryItem.builder().subCategory(subCategory)
                    .item(item).build());
        }
        itemAdminRepository.saveAll(testItems);
        subCategoryItemRepository.saveAll(testSubcategoryItems);

        return testItems;
    }
}
