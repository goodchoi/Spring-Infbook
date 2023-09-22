package infbook.infbook.domain.item.service;

import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategory;
import infbook.infbook.domain.category.repository.CategoryRepository;
import infbook.infbook.domain.category.repository.SubCategoryItemRepository;
import infbook.infbook.domain.category.repository.SubCategoryRepository;
import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.item.dto.ItemSaveDto;
import infbook.infbook.domain.item.repository.ItemAdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ItemAdminServiceTest {

    @Mock
    ItemAdminRepository itemAdminRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    SubCategoryRepository subCategoryRepository;


    @InjectMocks
    ItemAdminService itemService;



//    @Test
//    @DisplayName("폼으로 부터 입력받은 하위카테고리가 존재하지않으면 예외발생")
//    void itemSaveFailed() {
//        //given
//        ItemSaveDto targetDto = ItemSaveDto.builder()
//                .attachedImage(new MockMultipartFile("test.png", new byte[1]))
//                .categoryId(1L)
//                .subCategories(List.of(1L))
//                .build();
//
//        given(subCategoryRepository.findById(anyLong()))
//                .willReturn(Optional.empty()); //만약 번호로 검색한 하위 카테고리 결과가 없다면
//        given(categoryRepository.getReferenceById(anyLong()))
//                .willReturn(Category.builder().build());
//
//
//        assertThatThrownBy(() -> itemService.enrollItem(targetDto))
//                .isInstanceOf(Exception.class).hasMessageContaining("카테고리");
//    }
//
//
//    @Test
//    @DisplayName("저장 성공시 저장한 엔티티 반환")
//    void itemSaveSuccess() throws IOException {
//        //given
//        ItemSaveDto targetDto = ItemSaveDto.builder()
//                .name("testbook")
//                .author("me")
//                .attachedImage(new MockMultipartFile("test.png", new byte[1]))
//                .categoryId(1L)
//                .subCategories(List.of(1L))
//                .build();
//        given(subCategoryRepository.findById(anyLong()))
//                .willReturn(Optional.of(SubCategory.builder().build())); //만약 번호로 검색한 하위 카테고리 결과가 없다면
//        given(categoryRepository.getReferenceById(anyLong()))
//                .willReturn(any(Category.class));
//
//        //when
//        Item savedItem = itemService.enrollItem(targetDto);
//        //then
//        assertThat(savedItem.getName()).isEqualTo("testbook");
//        assertThat(savedItem.getAuthor()).isEqualTo("me");
//    }


}