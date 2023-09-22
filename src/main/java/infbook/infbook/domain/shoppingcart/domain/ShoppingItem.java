package infbook.infbook.domain.shoppingcart.domain;

import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.exception.InsufficentQuantityException;
import infbook.infbook.exception.InvalidQuantityException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShoppingItem {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "shopping_item_id",columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
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
        //shoppingCart.getShoppingItems().add(this);
    }

    public void changeQuantity(Integer requestQuantity) {
        this.quantity = requestQuantity;
    }
}
