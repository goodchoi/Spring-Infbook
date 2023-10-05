package infbook.infbook.abstractTest;

import infbook.infbook.domain.category.repository.CategoryRepository;
import infbook.infbook.domain.category.repository.SubCategoryItemRepository;
import infbook.infbook.domain.category.repository.SubCategoryRepository;
import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.item.repository.ItemAdminRepository;
import infbook.infbook.domain.item.repository.ItemRepository;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.domain.order.repository.OrderItemRepository;
import infbook.infbook.domain.order.repository.OrderRepository;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.domain.ShoppingItem;
import infbook.infbook.domain.shoppingcart.dto.CartPostRequest;
import infbook.infbook.domain.shoppingcart.repository.ShoppingCartRepository;
import infbook.infbook.domain.shoppingcart.repository.ShoppingItemRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTest {

    @Mock
    protected BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    protected MemberRepository memberRepository;
    @Mock
    protected ItemAdminRepository itemAdminRepository;
    @Mock
    protected ItemRepository itemRepository;
    @Mock
    protected ShoppingCartRepository shoppingCartRepository;
    @Mock
    protected ShoppingItemRepository shoppingItemRepository;
    @Mock
    protected CategoryRepository categoryRepository;
    @Mock
    protected SubCategoryRepository subCategoryRepository;
    @Mock
    protected SubCategoryItemRepository subCategoryItemRepository;
    @Mock
    protected OrderRepository orderRepository;
    @Mock
    protected OrderItemRepository orderItemRepository;

    @Mock
    protected Member member;
    @Mock
    protected Item item;
    @Mock
    protected ShoppingCart shoppingCart;
    @Mock
    protected ShoppingItem shoppingItem;
}
