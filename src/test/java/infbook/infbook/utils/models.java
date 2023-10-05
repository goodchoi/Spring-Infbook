package infbook.infbook.utils;

import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategory;
import infbook.infbook.domain.category.domain.SubCategoryItem;
import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.domain.UserLevel;
import infbook.infbook.domain.model.Address;
import infbook.infbook.domain.order.domain.*;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.domain.ShoppingItem;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class models {

    //Address
    public static final Address ADDRESS = getAddress();

    private static Address getAddress() {
        return Address.builder()
                .zipcode(ADDRESS_ZIPCODE)
                .city(ADDRESS_CITY)
                .detailedAddress(ADDRESS_DETAILEDADDRESS)
                .street(ADDRESS_STREET)
                .build();
    }

    public static final String ADDRESS_ZIPCODE = "00000";
    public static final String ADDRESS_STREET = "테스트 도로명";
    public static final String ADDRESS_CITY = "테스트 도시";
    public static final String ADDRESS_DETAILEDADDRESS = "테스트 상세주소";

    //member
    public static final Member MEMBER = getMEMBER();

    private static Member getMEMBER() {
        return Member.builder()
                .name(MEMBER_NAME)
                .accountId(MEMBER_ACCOUNTID)
                .password(MEMBER_PASSWORD)
                .birthDate(MEMBER_BIRTHDATE)
                .email(MEMBER_EMAIL)
                .telephone(MEMBER_TELEPHONE)
                .userLevel(MEMBER_USERLEVEL)
                .build();
    }

    public static final Long MEMBER_ID = 1L;
    public static final String MEMBER_NAME = "테스트멤버이름";
    public static final String MEMBER_ACCOUNTID = "test1234";
    public static final String MEMBER_PASSWORD = "adsfadsf22!";
    public static final String MEMBER_EMAIL = "test@test.com";
    public static final LocalDate MEMBER_BIRTHDATE = LocalDate.of(1995, 2, 22);
    public static final String MEMBER_TELEPHONE = "010-0000-000";
    public static final UserLevel MEMBER_USERLEVEL = UserLevel.ROLE_USER;

    //shoppingcart
    //  public static final ShoppingCart SHOPPING_CART = getShoppingCart();

    public static final Long SHOPPINGCART_ID = 1L;


    //shoppingitem
    // public static final ShoppingItem SHOPPING_ITEM = getShoppingItem();
    public static final Long SHOPPINGTEM_ID = 1L;
    public static final int SHOPPINGTEM_QUANTITY = 5;

    //item
    //  public static final Item ITEM = getItem();
    public static final Long ITEM_ID = 1L;
    public static final String ITEM_NAME = "테스트 도서명";
    public static final String ITEM_PUBLISHER = "테스트 출판사명";
    public static final String ITEM_AUTHOR = "테스트 저자명";
    public static final Integer ITEM_PRICE = 10000;
    public static final String ITEM_ISBN = "0000000000000";
    public static final LocalDate ITEM_PUBLICATIONDATE = LocalDate.of(1994, 3, 22);
    public static final int ITEM_PAGENUMBER = 100;
    public static final String ITEM_INDEX = "테스트 도서 목차";
    public static final int ITEM_STOCKQUANTITY = 5;


    //category
    //  public static final Category CATEGORY = getCategory();
    public static final Long CATEGORY_ID = 1L;
    public static final String CATEGORY_NAME = "테스트 카테고리명";
    public static final String CATEGORY_ENG_NAME = "test_category";

    //subcategory
    // public static final SubCategory SUB_CATEGORY = getSubCategory();
    public static final Long SUBCATEGORY_ID = 1L;
    public static final String SUBCATEGORY_NAME = "테스트 하위 카테고리명";
    public static final String SUBCATEGORY_ENG_NAME = "test_subcategory";

    //order
    //public static final Order ORDER = getOrder();
    public static final Long ORDER_ID = 1L;
    public static final int ORDER_TOTALPRICE = 10000;
    public static final LocalDateTime ORDER_ORDERDATE = LocalDateTime.now();
    public static final OrderStatus ORDER_ORDERSTATUS = OrderStatus.SUCCESS;

    //orderItem
    // public static final OrderItem ORDER_ITEM = getOrderItem();
    public static final Long ORDERITEM_ID = 1L;
    public static final int ORDERITEM_QUANTITY = 1;

    //delivery
    // public static final Delivery DELIVERY = getDelivery();
    public static final Long DELVIERY_ID = 1L;
    public static final DeliveryStatus DELVIERY_STATUS = DeliveryStatus.READY;
}
