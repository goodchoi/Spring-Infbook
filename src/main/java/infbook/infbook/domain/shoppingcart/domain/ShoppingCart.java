package infbook.infbook.domain.shoppingcart.domain;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShoppingCart extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopping_cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    //@Builder.Default
    @OneToMany(mappedBy = "shoppingCart")
    private List<ShoppingItem> shoppingItems = new ArrayList<>();

    @Builder
    private ShoppingCart(Member member, List<ShoppingItem> shoppingItems) {
        this.member = member;
        this.shoppingItems = shoppingItems;
    }
}
