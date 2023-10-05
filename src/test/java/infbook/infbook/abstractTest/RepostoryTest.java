package infbook.infbook.abstractTest;

import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategory;
import infbook.infbook.domain.category.domain.SubCategoryItem;
import infbook.infbook.domain.category.repository.CategoryRepository;
import infbook.infbook.domain.category.repository.SubCategoryItemRepository;
import infbook.infbook.domain.category.repository.SubCategoryRepository;
import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.item.repository.ItemAdminRepository;
import infbook.infbook.domain.item.repository.ItemRepository;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.domain.model.Address;
import infbook.infbook.domain.order.domain.Delivery;
import infbook.infbook.domain.order.domain.Order;
import infbook.infbook.domain.order.domain.OrderItem;
import infbook.infbook.domain.order.repository.DeliveryRepository;
import infbook.infbook.domain.order.repository.OrderItemRepository;
import infbook.infbook.domain.order.repository.OrderRepository;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.domain.ShoppingItem;
import infbook.infbook.domain.shoppingcart.repository.ShoppingCartRepository;
import infbook.infbook.domain.shoppingcart.repository.ShoppingItemRepository;
import infbook.infbook.utils.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static infbook.infbook.utils.models.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@Slf4j
@Transactional
@Import({TestConfig.class})
@DataJpaTest
public abstract class RepostoryTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ShoppingCartRepository shoppingCartRepository;

    @Autowired
    protected ShoppingItemRepository shoppingItemRepository;

    @Autowired
    protected ItemAdminRepository itemAdminRepository;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected SubCategoryItemRepository subCategoryItemRepository;

    @Autowired
    protected SubCategoryRepository subCategoryRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderItemRepository orderItemRepository;

    @Autowired
    protected DeliveryRepository deliveryRepository;

    protected Address address;
    protected Member savedMember;
    protected ShoppingCart savedShoppingCart;
    protected ShoppingItem savedShoppingItem;
    protected SubCategory savedSubCategory;
    protected Category savedCategory;
    protected Item savedItem;
    protected SubCategoryItem savedSubCategoryItem;
    protected Order savedOrder;
    protected OrderItem savedOrderItem;
    protected Delivery savedDelivery;

    @BeforeEach
    void createAllEntity() {

        //Member
        savedMember = Member.builder()
                .name(MEMBER_NAME)
                .accountId(MEMBER_ACCOUNTID)
                .password(MEMBER_PASSWORD)
                .birthDate(MEMBER_BIRTHDATE)
                .email(MEMBER_EMAIL)
                .telephone(MEMBER_TELEPHONE)
                .address(ADDRESS)
                .userLevel(MEMBER_USERLEVEL)
                .build();

        memberRepository.save(savedMember);

        //ShoppingCart
        savedShoppingCart = ShoppingCart.builder()
                .member(savedMember)
                .build();

        shoppingCartRepository.save(savedShoppingCart);


        //category
        savedCategory = Category.builder()
                .name(CATEGORY_NAME)
                .engName(CATEGORY_ENG_NAME)
                .build();
        categoryRepository.save(savedCategory);
        //SubCategory
        savedSubCategory = SubCategory.builder()
                .name(SUBCATEGORY_NAME)
                .parent(savedCategory)
                .engName(SUBCATEGORY_ENG_NAME)
                .build();
        subCategoryRepository.save(savedSubCategory);

        //item
        savedItem = Item.builder()
                .price(ITEM_PRICE)
                .index(ITEM_INDEX)
                .pageNumber(ITEM_PAGENUMBER)
                .author(ITEM_AUTHOR)
                .fileName("test")
                .category(savedCategory)
                .name(ITEM_NAME)
                .publisher(ITEM_PUBLISHER)
                .publicationDate(ITEM_PUBLICATIONDATE)
                .stockQuantity(ITEM_STOCKQUANTITY)
                .isbn(ITEM_ISBN)
                .build();

        itemAdminRepository.save(savedItem);

        //subcategory_item
        savedSubCategoryItem = SubCategoryItem.builder()
                .subCategory(savedSubCategory)
                .item(savedItem)
                .build();

        subCategoryItemRepository.save(savedSubCategoryItem);

        //ShoppingItem
        savedShoppingItem = ShoppingItem.builder()
                .shoppingCart(savedShoppingCart)
                .item(savedItem)
                .quantity(SHOPPINGTEM_QUANTITY)
                .build();
        shoppingItemRepository.save(savedShoppingItem);



        //delivery
        savedDelivery = Delivery.builder()
                .deliveryStatus(DELVIERY_STATUS)
                .address(ADDRESS)
                .build();

        deliveryRepository.save(savedDelivery);

        //Order
        savedOrder = Order.builder()
                .orderStatus(ORDER_ORDERSTATUS)
                .orderDate(ORDER_ORDERDATE)
                .totalPrice(ORDER_TOTALPRICE)
                .member(savedMember)
                .build();

        orderRepository.save(savedOrder);

        savedOrderItem = OrderItem.builder()
                .item(savedItem)
                .quantity(ORDERITEM_QUANTITY)
                .order(savedOrder)
                .build();

        orderItemRepository.save(savedOrderItem);
    }
}
