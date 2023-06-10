package infbook.infbook.domain.category.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryItemPk implements Serializable {
    private Long item;
    private Long subCategory;
}
