package infbook.infbook.domain.category.domain;

import infbook.infbook.domain.item.domain.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(SubCategoryItemPk.class)
@Table(name = "subcategory_item")
public class SubCategoryItem {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    @Builder
    public SubCategoryItem(Item item, SubCategory subCategory) {
        this.item = item;
        this.subCategory = subCategory;
    }
}
