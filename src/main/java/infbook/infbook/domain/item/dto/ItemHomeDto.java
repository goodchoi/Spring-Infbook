package infbook.infbook.domain.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ItemHomeDto {
    private Long id;
    private String fileName;
    private String name;

    @QueryProjection
    public ItemHomeDto(Long id, String fileName, String name) {
        this.id = id;
        this.fileName = fileName;
        this.name = name;
    }
}
