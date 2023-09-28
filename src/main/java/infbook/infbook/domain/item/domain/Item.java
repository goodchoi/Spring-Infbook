package infbook.infbook.domain.item.domain;

import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategoryItem;
import infbook.infbook.domain.item.dto.ItemUpdateDto;
import infbook.infbook.domain.item.dto.ItemUpdateFormDto;
import infbook.infbook.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item" ,indexes = {
        @Index(name = "idx_item_pub_date",columnList = "publicationDate"),
        @Index(name = "idx_item_price",columnList = "price"),
        @Index(name = "idx_item_stock",columnList = "stockQuantity")
})
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(60)")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(60)")
    private String publisher;


    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String author;

    @Column(nullable = false, columnDefinition = "varchar(13)")
    private String isbn;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String fileName;

    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDate publicationDate;

    @Column(nullable = true)
    private String subTitle;

    @Column(nullable = false)
    private Integer pageNumber;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "item")
    private List<SubCategoryItem> subCategories = new ArrayList<>();

    @Column(name = "indexes",nullable = false, columnDefinition = "text")
    private String index;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Builder
    public Item(String name, String publisher, String author, String isbn, String fileName, LocalDate publicationDate, Integer pageNumber,String subTitle, Integer price, Category category, List<SubCategoryItem> subCategories, String index, Integer stockQuantity) {
        this.name = name;
        this.publisher = publisher;
        this.author = author;
        this.isbn = isbn;
        this.fileName = fileName;
        this.publicationDate = publicationDate;
        this.pageNumber = pageNumber;
        this.price = price;
        this.category = category;
        this.subCategories = subCategories;
        this.index = index;
        this.stockQuantity = stockQuantity;
        this.subTitle = subTitle;
    }


    public ItemUpdateFormDto getItemSaveDto() {
        return ItemUpdateFormDto.builder()
                .id(this.id)
                .fileName(this.fileName)
                .name(this.name)
                .isbn(this.getIsbn())
                .categoryId(this.category.getId())
                .subCategories(this.subCategories.stream()
                        .map(s -> s.getSubCategory().getId())
                        .collect(Collectors.toList()))
                .author(this.getAuthor())
                .publisher(this.getPublisher())
                .price(this.getPrice())
                .index(this.getIndex())
                .publicationDate(this.getPublicationDate())
                .stockQuantity(this.getStockQuantity())
                .pageNumber(this.getPageNumber())
                .build();
    }


    public void changeFileName(String fileName) {
        this.fileName = fileName;
    }

    public void changeSubCategories(List<SubCategoryItem> subCategories) {
        this.subCategories = subCategories;
    }

    public void changeStock(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void updateItem(ItemUpdateDto dto, Category category) {
        this.fileName = dto.getFileName();
        this.price = dto.getPrice();
        this.stockQuantity = dto.getStockQuantity();
        this.category = category;
    }


}
