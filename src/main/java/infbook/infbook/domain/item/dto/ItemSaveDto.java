package infbook.infbook.domain.item.dto;


import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.item.domain.Item;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class ItemSaveDto {

    private MultipartFile attachedImage;

    @NotEmpty
    private String name;

    @NotEmpty
    private String subTitle;

    @NotEmpty
    private String publisher;

    @NotEmpty
    private String author;

    @NotBlank
    private String isbn;

    @NotNull
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate publicationDate;

    @NotNull
    @Min(0)
    private Integer pageNumber;

    @NotNull
    @Min(1000)
    private Integer price;

    @NotNull
    private Long categoryId;

    @Size(min = 1)
    private List<Long> subCategories;

    @NotEmpty
    private String index;

    @NotNull
    @Min(0)
    private Integer stockQuantity;

    @Builder
    public ItemSaveDto(MultipartFile attachedImage, String name, String subTitle,String publisher, String author, String isbn, LocalDate publicationDate, Integer pageNumber, Integer price, Long categoryId, List<Long> subCategories, String index, Integer stockQuantity) {

        this.attachedImage = attachedImage;
        this.name = name;
        this.publisher = publisher;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.pageNumber = pageNumber;
        this.subTitle = subTitle;
        this.price = price;
        this.categoryId = categoryId;
        if (Objects.isNull(subCategories)) {
            this.subCategories = new ArrayList<>();
        } else {
            this.subCategories = subCategories;
        }
        this.index = index;
        this.stockQuantity = stockQuantity;
    }

    public Item getSaveItem(Category refCategory) {
        return Item.builder()
                .name(this.name)
                .isbn(this.getIsbn())
                .category(refCategory)
                .author(this.getAuthor())
                .subTitle(this.subTitle)
                .publisher(this.getPublisher())
                .price(this.getPrice())
                .index(this.getIndex())
                .publicationDate(this.getPublicationDate())
                .stockQuantity(this.getStockQuantity())
                .pageNumber(this.getPageNumber())
                .build();
    }
}
