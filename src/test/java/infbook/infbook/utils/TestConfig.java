package infbook.infbook.utils;

import com.querydsl.jpa.impl.JPAQueryFactory;
import infbook.infbook.domain.item.repository.ItemRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }


    @Bean
    public ItemRepository itemRepository() {
        return new ItemRepository(jpaQueryFactory());
    }
}
