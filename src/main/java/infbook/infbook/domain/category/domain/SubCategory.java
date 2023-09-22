package infbook.infbook.domain.category.domain;

import infbook.infbook.global.util.CategoryUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subcategory_id")
    private Long id;

    private String name;
    private String engName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category parent;

    @Builder
    public SubCategory(String name, Category parent ,String engName) {
        this.name = name;
        this.parent = parent;
        this.engName =engName;
    }
}
