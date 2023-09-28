package infbook.infbook.domain.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemSingleDto {

    private Long id;

    private String name;

    private String publisher;

    private String author;

    private String isbn;

    private String fileName;

    private LocalDate publicationDate;

    private Integer pageNumber;

    private Integer price;

    private String subTitle;

    private String index;

    @QueryProjection
    public ItemSingleDto(Long id, String name, String subTitle,String publisher, String author, String isbn, String fileName, LocalDate publicationDate, Integer pageNumber, Integer price, String index) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.author = author;
        this.isbn = isbn;
        this.fileName = fileName;
        this.publicationDate = publicationDate;
        this.pageNumber = pageNumber;
        this.price = price;
        this.index = index;
        this.subTitle = subTitle;
    }
}
