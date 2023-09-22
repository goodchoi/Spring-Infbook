package infbook.infbook.domain.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategoryItem;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ItemListDto {
    private Long id;

    private String name;

    private String publisher;

    private String author;

    private String fileName;

    private LocalDate publicationDate;

    private Integer price;

    @QueryProjection
    public ItemListDto(Long id, String name, String publisher, String author, String fileName, LocalDate publicationDate, Integer price) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.author = author;
        this.fileName = fileName;
        this.publicationDate = publicationDate;
        this.price = price;
    }
}
