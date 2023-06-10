package infbook.infbook.domain.shoppingcart.domain;

import infbook.infbook.domain.item.domain.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShoppingItem {

    @Id
    @GeneratedValue
    @Column(name = "shopping_item_id")
    private Long id;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    @Builder
    public ShoppingItem(int quantity, Item item, ShoppingCart shoppingCart) {
        this.quantity = quantity;
        this.item = item;
        this.shoppingCart = shoppingCart;
    }
}
