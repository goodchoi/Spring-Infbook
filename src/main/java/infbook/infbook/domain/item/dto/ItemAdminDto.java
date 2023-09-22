package infbook.infbook.domain.item.dto;


import infbook.infbook.domain.item.domain.Item;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ItemAdminDto {

    private String fileName;
    private String name;
    private String publisher;
    private String author;
    private String isbn;
    private LocalDate publicationDate;
    private Integer pageNumber;
    private Integer price;
    private String categoryName;
    private List<String> subCategories;
    private String index;
    private Integer stockQuantity;

    @Builder
    public ItemAdminDto(String fileName,String name, String publisher, String author, String isbn, LocalDate publicationDate, Integer pageNumber, Integer price, String categoryName, List<String> subCategories, String index, Integer stockQuantity) {
        this.fileName = fileName;
        this.name = name;
        this.publisher = publisher;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.pageNumber = pageNumber;
        this.price = price;
        this.categoryName = categoryName;
        this.subCategories = subCategories;
        this.index = index;
        this.stockQuantity = stockQuantity;
    }

    public static ItemAdminDto getItemAdminDto(Item item) {
        return ItemAdminDto.builder()
                .name(item.getName())
                .isbn(item.getIsbn())
                .categoryName(item.getCategory().getName())
                .subCategories(
                        item.getSubCategories().stream()
                                .map(s -> s.getSubCategory().getName())
                                .toList()
                )
                .author(item.getAuthor())
                .publisher(item.getPublisher())
                .price(item.getPrice())
                .index(item.getIndex())
                .publicationDate(item.getPublicationDate())
                .stockQuantity(item.getStockQuantity())
                .pageNumber(item.getPageNumber())
                .fileName(item.getFileName())
                .build();
    }
}
