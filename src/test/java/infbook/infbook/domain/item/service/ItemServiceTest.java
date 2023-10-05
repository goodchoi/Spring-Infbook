package infbook.infbook.domain.item.service;

import infbook.infbook.abstractTest.ServiceTest;
import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategory;
import infbook.infbook.domain.category.repository.CategoryRepository;
import infbook.infbook.domain.category.repository.SubCategoryItemRepository;
import infbook.infbook.domain.category.repository.SubCategoryRepository;
import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.item.dto.ItemSaveDto;
import infbook.infbook.domain.item.repository.ItemRepository;
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
class ItemServiceTest extends ServiceTest {

    @InjectMocks
    ItemAdminService itemService;


    @Test
    @DisplayName("폼으로 부터 입력받은 하위카테고리가 존재하지않으면 예외발생")
    void itemSaveFailed() {
        //given

    }


    @Test
    @DisplayName("저장 성공시 저장한 엔티티 반환")
    void itemSaveSuccess() throws IOException {
        //given

    }


}