package infbook.infbook.domain.item.dto;


import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.item.domain.Item;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class ItemUpdateFormDto {

    private Long id;

    private MultipartFile attachedImage;

    private String fileName;

    private String name;

    private String publisher;

    private String author;

    private String isbn;

    private LocalDate publicationDate;

    private Integer pageNumber;

    private Integer price;

    private Long categoryId;

    private List<Long> subCategories = new ArrayList<>();

    private String index;

    private Integer stockQuantity;

    @Builder
    public ItemUpdateFormDto(Long id, String fileName, String name, String publisher, String author, String isbn, LocalDate publicationDate, Integer pageNumber, Integer price, Long categoryId, List<Long> subCategories, String index, Integer stockQuantity) {

        this.id = id;
        this.fileName = fileName;
        this.name = name;
        this.publisher = publisher;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.pageNumber = pageNumber;
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

    public Item getUpdateItemDto(Category refCategory) {
        return Item.builder()
                .name(this.name)
                .isbn(this.getIsbn())
                .category(refCategory)
                .author(this.getAuthor())
                .publisher(this.getPublisher())
                .price(this.getPrice())
                .index(this.getIndex())
                .publicationDate(this.getPublicationDate())
                .stockQuantity(this.getStockQuantity())
                .pageNumber(this.getPageNumber())
                .build();
    }
}
