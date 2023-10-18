package infbook.infbook.domain.item.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import infbook.infbook.domain.category.domain.QCategory;
import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.item.dto.*;
import infbook.infbook.domain.order.domain.QOrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static infbook.infbook.domain.category.domain.QCategory.*;
import static infbook.infbook.domain.category.domain.QSubCategory.*;
import static infbook.infbook.domain.category.domain.QSubCategoryItem.*;
import static infbook.infbook.domain.item.domain.QItem.*;
import static infbook.infbook.domain.order.domain.QOrderItem.*;

@RequiredArgsConstructor
@Repository
public class ItemRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<ItemListDto> searchAllBySubcategoryName(String subcategryName, Pageable pageable) {
        List<ItemListDto> itemList = jpaQueryFactory
                .select(new QItemListDto(
                        item.id, item.name,item.subTitle, item.publisher, item.author, item.fileName
                        , item.publicationDate, item.price
                ))
                .from(item)
                .join(subCategoryItem)
                .on(subCategoryItem.item.eq(item))
                .join(subCategory)
                .on(subCategoryItem.subCategory.eq(subCategory))
                .where(
                        subCategory.engName.eq(subcategryName),
                        item.stockQuantity.gt(0)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ordercomplex(pageable))
                .fetch();//subcategoryItem, Subcategory innerjoin 한다음에  where 로 subcategory name == 하면 될듯?


        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(item.count())
                .from(item)
                .join(subCategoryItem)
                .on(subCategoryItem.item.eq(item))
                .join(subCategory)
                .on(subCategoryItem.subCategory.eq(subCategory))
                .where(
                        subCategory.engName.eq(subcategryName),
                        item.stockQuantity.gt(0)
                );
        return PageableExecutionUtils.getPage(itemList, pageable, countQuery::fetchOne);
    }

    public Page<ItemListDto> searchAllByCategoryName(String categoryName, Pageable pageable) {
        List<ItemListDto> itemList = jpaQueryFactory
                .select(new QItemListDto(
                        item.id, item.name, item.subTitle,item.publisher, item.author, item.fileName
                        , item.publicationDate, item.price
                ))
                .from(item)
                .join(category)
                .on(item.category.eq(category))
                .where(
                        category.engName.eq(categoryName),
                        item.stockQuantity.gt(0)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ordercomplex(pageable))
                .fetch();


        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(item.count())
                .from(item)
                .join(category)
                .on(item.category.eq(category))
                .where(
                        category.engName.eq(categoryName),
                        item.stockQuantity.gt(0)
                );
        return PageableExecutionUtils.getPage(itemList, pageable, countQuery::fetchOne);
    }

    public Optional<ItemSingleDto> findItemSingDtoById(Long id) {

        ItemSingleDto itemSingleDto = jpaQueryFactory.
                select(new QItemSingleDto(item.id, item.name, item.subTitle,item.publisher, item.author, item.isbn
                        , item.fileName, item.publicationDate, item.pageNumber, item.price, item.index))
                .from(item)
                .where(item.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(itemSingleDto);
    }



    private OrderSpecifier<?> ordercomplex(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                    return new OrderSpecifier(order.isDescending() ? Order.DESC : Order.ASC
                            , Expressions.path(item.getClass(), order.getProperty()));
            }
        }
        return null;
    }


    /**
     * 판매순 아이템의 필드로 바로 정렬하는것이 아닌
     * 주문-상품 테이블의 추가적인 조인과 그룹핑이 필요하다.
     */
    public Page<ItemListDto> searchPopularBySubCategoryName(String subcategoryName, Pageable pageable) {
        List<Tuple> sales = jpaQueryFactory
                .select(new QItemListDto(
                        item.id, item.name, item.subTitle,item.publisher, item.author, item.fileName
                        , item.publicationDate, item.price
                ), orderItem.quantity.sum().as("sales"))
                .from(item)
                .join(subCategoryItem)
                .on(subCategoryItem.item.eq(item))
                .join(subCategory)
                .on(subCategoryItem.subCategory.eq(subCategory).and(subCategory.engName.eq(subcategoryName)))
                .leftJoin(orderItem)
                .on(orderItem.item.eq(item))
                .where(
                        item.stockQuantity.gt(0)
                )
                .groupBy(item.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderItem.quantity.sum().desc(),item.publicationDate.desc())
                .fetch();

        List<ItemListDto> itemList = sales.stream().map(t -> t.get(0, ItemListDto.class)).toList();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(item.count())
                .from(item)
                .join(subCategoryItem)
                .on(subCategoryItem.item.eq(item))
                .join(subCategory)
                .on(subCategoryItem.subCategory.eq(subCategory).and(subCategory.engName.eq(subcategoryName)))
                .where(
                        item.stockQuantity.gt(0)
                );
        return PageableExecutionUtils.getPage(itemList, pageable, countQuery::fetchOne);
    }

    public Page<ItemListDto> searchPopularByCategoryName(String categoryName, Pageable pageable) {
        List<Tuple> sales = jpaQueryFactory
                .select(new QItemListDto(
                        item.id, item.name, item.subTitle, item.publisher, item.author, item.fileName
                        , item.publicationDate, item.price
                ), orderItem.quantity.sum().as("sales"))
                .from(item)
                .join(category)
                .on(item.category.eq(category).and(category.engName.eq(categoryName)))
                .leftJoin(orderItem)
                .on(orderItem.item.eq(item))
                .where(
                        item.stockQuantity.gt(0)
                )
                .groupBy(item.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderItem.quantity.sum().desc(),item.publicationDate.desc())
                .fetch();

        List<ItemListDto> itemList = sales.stream().map(t -> t.get(0, ItemListDto.class)).toList();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(item.count())
                .from(item)
                .join(category)
                .on(item.category.eq(category).and(category.engName.eq(categoryName)))
                .where(
                        item.stockQuantity.gt(0)
                );
        return PageableExecutionUtils.getPage(itemList, pageable, countQuery::fetchOne);
    }

    public Page<ItemListDto> searchItemByKeyWord(String keyword, Pageable pageable) {
        List<ItemListDto> itemList = jpaQueryFactory
                .select(new QItemListDto(
                        item.id, item.name, item.subTitle,item.publisher, item.author, item.fileName
                        , item.publicationDate, item.price
                ))
                .from(item)
                .where(item.name.contains(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ordercomplex(pageable))
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(item.count())
                .from(item)
                .where(item.name.contains(keyword));
        return PageableExecutionUtils.getPage(itemList, pageable, countQuery::fetchOne);
    }

    public Page<ItemListDto> searchPopularItemByKeyWord(String keyword, Pageable pageable) {
        List<Tuple> sales = jpaQueryFactory
                .select(new QItemListDto(
                        item.id, item.name, item.subTitle, item.publisher, item.author, item.fileName
                        , item.publicationDate, item.price
                ), orderItem.quantity.sum().as("sales"))
                .from(item)
                .leftJoin(orderItem)
                .on(orderItem.item.eq(item))
                .where(item.name.contains(keyword))
                .groupBy(item.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderItem.quantity.sum().desc(),item.publicationDate.desc())
                .fetch();

        List<ItemListDto> itemList = sales.stream().map(t -> t.get(0, ItemListDto.class)).toList();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(item.count())
                .from(item)
                .where(item.name.contains(keyword));
        return PageableExecutionUtils.getPage(itemList, pageable, countQuery::fetchOne);
    }

    /**
     * 판매량이 높은순서로부터 5개의 리스트를 확보한다.
     * 판매량이 같을경우 출간일이 빠른순으로.
     */
    public List<ItemHomeDto> findBestSellers(){
        return jpaQueryFactory
                .select(new QItemHomeDto(
                        item.id,item.fileName,item.name
                ), orderItem.quantity.sum().as("sales"))
                .from(item)
                .leftJoin(orderItem)
                .on(orderItem.item.eq(item))
                .groupBy(item.id)
                .limit(5)
                .orderBy(orderItem.quantity.sum().desc(), item.publicationDate.desc())
                .fetch()
                .stream()
                .map(tuple -> tuple.get(0, ItemHomeDto.class))
                .toList();
    }
}
