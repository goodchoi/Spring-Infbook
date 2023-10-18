package infbook.infbook.domain.item.service;

import com.querydsl.core.types.Order;
import infbook.infbook.domain.item.dto.ItemHomeDto;
import infbook.infbook.domain.item.dto.ItemListDto;
import infbook.infbook.domain.item.dto.ItemSingleDto;
import infbook.infbook.domain.item.repository.ItemAdminRepository;
import infbook.infbook.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    public Page<ItemListDto> getItemOfSubCategory(String subcategoryName, Pageable pageable) {
        return itemRepository.searchAllBySubcategoryName(subcategoryName, convertPageable(pageable));
    }

    public Page<ItemListDto> getItemOfCategory(String categoryName, Pageable pageable) {
        return itemRepository.searchAllByCategoryName(categoryName, convertPageable(pageable));
    }

    public ItemSingleDto getItemOfId(Long id) {

        Optional<ItemSingleDto> itemSingDtoById = itemRepository.findItemSingDtoById(id);

        return itemSingDtoById.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"검색하는 상품의 Id가 없습니다."));
    }

    public Page<ItemListDto> getPopularItemOfSubCategeory(String subcategoryName, Pageable pageable) {
        return itemRepository.
                searchPopularBySubCategoryName(subcategoryName,PageRequest.of(pageable.getPageNumber(),fixPageSize(pageable.getPageSize())));
    }

    public Page<ItemListDto> getPopularItemOfCategeory(String categoryName, Pageable pageable) {
        return itemRepository.searchPopularByCategoryName(categoryName,PageRequest.of(pageable.getPageNumber(),fixPageSize(pageable.getPageSize())));
    }

    public Page<ItemListDto> getItemOfKeyword(String keyword, Pageable pageable) {
        return itemRepository.searchItemByKeyWord(keyword, convertPageable(pageable));
    }

    public Page<ItemListDto> getPopularItemOfKeyword(String keyword, Pageable pageable) {
        return itemRepository.searchPopularItemByKeyWord(keyword, PageRequest.of(pageable.getPageNumber(),fixPageSize(pageable.getPageSize())));
    }

    /**
     * 베스트셀러는 홈화면에서 요청되므로 사용 빈도가 굉장히 높고, 쿼리문 비용이 크기때문에 스프링 로컬 캐시를 사용한다.
     */
    @Cacheable(cacheNames = "bestSellerCache")
    public List<ItemHomeDto> getBestSellers() {
        return itemRepository.findBestSellers();
    }

    private Pageable convertPageable(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = fixPageSize(pageable.getPageSize());

        String property = pageable.getSort().get().findFirst()
                .orElseThrow(() -> new IllegalStateException("sort 파라미터가 없습니다."))
                .getProperty();

        if (property.equals("highprice")) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "price"));
        }
        if (property.equals("lowprice")) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "price"));
        }

        return  PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "publicationDate"));
    }

    private int fixPageSize(int pageSize){
        return (pageSize == 5 || pageSize == 10) ? pageSize : 5;
    }
}
