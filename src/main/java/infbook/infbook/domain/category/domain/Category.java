package infbook.infbook.domain.category.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;
    private String engName;


    @OneToMany(mappedBy = "parent")
    private List<SubCategory> children;

    @Builder
    public Category(String name, String engName, List<SubCategory> children) {
        this.name = name;
        this.engName = engName;
        this.children = children;
    }
}
