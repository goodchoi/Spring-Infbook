package infbook.infbook.domain.item.domain;

import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategory;
import infbook.infbook.domain.category.domain.SubCategoryItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private String publisher;

    private String author;

    private String isbn;

    private LocalDate publicationDate;

    private int pageNumber;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category subCategory;

    @OneToMany(mappedBy = "item")
    private List<SubCategoryItem> subCategories;

    private String index;

    private int stockQunatity;

    @Builder
    public Item(String name, String publisher, String author, String isbn, LocalDate publicationDate, int pageNumber, Category subCategory, List<SubCategoryItem> subCategories, String index, int stockQunatity) {
        this.name = name;
        this.publisher = publisher;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.pageNumber = pageNumber;
        this.subCategory = subCategory;
        this.subCategories = subCategories;
        this.index = index;
        this.stockQunatity = stockQunatity;
    }
}
