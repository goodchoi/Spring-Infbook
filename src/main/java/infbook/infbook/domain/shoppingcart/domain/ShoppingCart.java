package infbook.infbook.domain.shoppingcart.domain;

import infbook.infbook.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShoppingCart {

    @Id @GeneratedValue
    @Column(name = "shopping_cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "shoppingCart")
    private List<ShoppingItem> shoppingItems;

    @Builder
    public ShoppingCart(Member member, List<ShoppingItem> shoppingItems) {
        this.member = member;
        this.shoppingItems = shoppingItems;
    }
}
