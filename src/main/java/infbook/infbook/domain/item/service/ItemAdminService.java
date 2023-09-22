package infbook.infbook.domain.item.service;

import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategoryItem;
import infbook.infbook.domain.category.repository.CategoryRepository;
import infbook.infbook.domain.category.repository.SubCategoryItemRepository;
import infbook.infbook.domain.category.repository.SubCategoryRepository;
import infbook.infbook.domain.item.domain.*;
import infbook.infbook.domain.item.dto.ItemAdminDto;
import infbook.infbook.domain.item.dto.ItemSaveDto;
import infbook.infbook.domain.item.dto.ItemUpdateDto;
import infbook.infbook.domain.item.dto.ItemUpdateFormDto;
import infbook.infbook.domain.item.repository.ItemAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemAdminService {

    @Value("${file.dir}")
    private String fileDir;

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ItemAdminRepository itemAdminRepository;
    private final SubCategoryItemRepository subCategoryItemRepository;


    public List<Category> getCategoryList() {
        return categoryRepository.findFetchAll();
    }


    public Item enrollItem(ItemSaveDto itemSaveDto) throws IOException {

        Category categoryReferenceById = categoryRepository.getReferenceById(itemSaveDto.getCategoryId());
        Item saveItem = itemSaveDto.getSaveItem(categoryReferenceById);
        String fileName = storeFile(itemSaveDto.getAttachedImage());
        saveItem.changeFileName(fileName);

        try {
            itemAdminRepository.save(saveItem);

            List<SubCategoryItem> subcategories = getSubCategories(itemSaveDto.getSubCategories(), saveItem);
            subCategoryItemRepository.saveAll(subcategories);

            saveItem.changeSubCategories(subcategories);
        } catch (Exception e) {
            Files.deleteIfExists(Path.of(makeFullPath(fileDir + fileName)));
            throw e;
        }
        return saveItem;
    }


    public ItemAdminDto getItemAdiminDtoByItemId(Long itemId) {
        Item findItem = itemAdminRepository.findFetchItem(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템이 존재하지 않습니다."));
        return ItemAdminDto.getItemAdminDto(findItem);
    }

    public ItemUpdateFormDto getItemSaveDtoByItemId(Long itemId) {
        Item findItem = itemAdminRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템이 존재하지 않습니다."));

        return findItem.getItemSaveDto();
    }

    public Long updateItem(Long itemId, ItemUpdateDto itemUpdateDto) throws IOException {

        Item findItem = itemAdminRepository.findFetchItem(itemId).orElseThrow(() -> new IllegalArgumentException("아이템이 존재하지 않습니다."));

        String changedFileName = editFile(itemUpdateDto.getAttachedImage(), itemUpdateDto.getFileName());
        itemUpdateDto.setFileName(changedFileName);
        if (!findItem.getCategory().getId().equals(itemUpdateDto.getCategoryId())) {
            findItem.updateItem(itemUpdateDto, categoryRepository.getReferenceById(itemUpdateDto.getCategoryId()));
        }
        List<SubCategoryItem> deleteSubcategoryies = findItem.getSubCategories().stream()
                .filter(s -> !itemUpdateDto.getSubCategories().contains(s.getSubCategory().getId()))
                .toList();

        if (!deleteSubcategoryies.isEmpty()) {
            subCategoryItemRepository.deleteAll(deleteSubcategoryies);
        }

        List<Long> originSubcategories = findItem.getSubCategories().stream().map(s -> s.getSubCategory().getId()).toList();
        List<Long> newSubcategories = itemUpdateDto.getSubCategories().stream()
                .filter(s -> !originSubcategories.contains(s))
                .toList();

        List<SubCategoryItem> newSubCategories = getSubCategories(newSubcategories, findItem);

        if (!newSubCategories.isEmpty()) {
            subCategoryItemRepository.saveAll(newSubCategories);
        }

        return findItem.getId();
    }

    private List<SubCategoryItem> getSubCategories(List<Long> subCategoryIdList, Item saveItem) {
        return subCategoryIdList.stream()
                .map(s -> SubCategoryItem.builder()
                        .subCategory(subCategoryRepository.getReferenceById(s))
                        .item(saveItem)
                        .build())
                .toList();
    }

    private String storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalStateException("등록할 이미지가 없습니다.");
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String newFileName = createNewFileName(originalFileName);
        multipartFile.transferTo(new File(makeFullPath(newFileName)));

        return newFileName;
    }

    private String editFile(MultipartFile multipartFile, String originFileName) throws IOException {
        if (multipartFile.isEmpty()) {
            return originFileName;
        }

        Files.deleteIfExists(Path.of(makeFullPath(fileDir + originFileName)));

        String changedName = changeFileNmae(originFileName, multipartFile.getOriginalFilename());
        multipartFile.transferTo(new File(makeFullPath(changedName)));

        return changedName;
    }

    private String changeFileNmae(String originFileName, String targeName) {
        int posOrigin = originFileName.lastIndexOf(".");
        String fileName = originFileName.substring(0, posOrigin);

        int posTarget = targeName.lastIndexOf(".");
        String ext = targeName.substring(posTarget + 1);
        return fileName + "." + ext;
    }


    private String createNewFileName(String originFileName) {
        int pos = originFileName.lastIndexOf(".");
        String ext = originFileName.substring(pos + 1);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }


    private String makeFullPath(String fileName) {
        return fileDir + fileName;
    }


}
