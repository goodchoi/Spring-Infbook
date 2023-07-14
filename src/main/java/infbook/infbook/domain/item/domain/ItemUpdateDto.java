package infbook.infbook.domain.item.domain;


import infbook.infbook.domain.category.domain.Category;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class ItemUpdateDto {

    private MultipartFile attachedImage;

    private String fileName;

    @NotNull
    @Min(1000)
    private Integer price;

    @NotNull
    private Long categoryId;

    @Builder.Default
    @Size(min = 1)
    private List<Long> subCategories = new ArrayList<>();


    @NotNull
    @Min(0)
    private Integer stockQuantity;

    @Builder
    public ItemUpdateDto(MultipartFile attachedImage,String fileName,Integer price, Long categoryId, List<Long> subCategories, String index, Integer stockQuantity) {
        this.fileName = fileName;
        this.attachedImage = attachedImage;
        this.price = price;
        this.categoryId = categoryId;
        if (Objects.isNull(subCategories)) {
            this.subCategories = new ArrayList<>();
        } else {
            this.subCategories = subCategories;
        }
        this.stockQuantity = stockQuantity;
    }

//    public Item getUpdateItem(Category refCategory) {
//        return Item.builder()
//                .name(this.name)
//                .isbn(this.getIsbn())
//                .category(refCategory)
//                .author(this.getAuthor())
//                .publisher(this.getPublisher())
//                .price(this.getPrice())
//                .index(this.getIndex())
//                .publicationDate(this.getPublicationDate())
//                .stockQuantity(this.getStockQuantity())
//                .pageNumber(this.getPageNumber())
//                .build();
//    }
}
