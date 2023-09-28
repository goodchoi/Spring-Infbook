package infbook.infbook.domain.item.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mysema.commons.lang.Pair;
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

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    private final AmazonS3 amazonS3;
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
        String fileName = uploadFile(itemSaveDto.getAttachedImage());
        saveItem.changeFileName(fileName);

        try {
            itemAdminRepository.save(saveItem);
            List<SubCategoryItem> subcategories = getSubCategories(itemSaveDto.getSubCategories(), saveItem);
            subCategoryItemRepository.saveAll(subcategories);
            saveItem.changeSubCategories(subcategories);
        } catch (Exception e) {
            e.printStackTrace();
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

    public Long updateItem(Long itemId, ItemUpdateDto itemUpdateDto) {

        Item findItem = itemAdminRepository.findFetchItem(itemId).orElseThrow(() -> new IllegalArgumentException("아이템이 존재하지 않습니다."));

        // String changedFileName = editFile(itemUpdateDto.getAttachedImage(), itemUpdateDto.getFileName());
        //  itemUpdateDto.setFileName(changedFileName);
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

    private String uploadFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalStateException("등록할 이미지가 없습니다.");
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String contentType = ""; //AmazonS3로 put 요청을 보낼때 필수로 등록해야함 만일 등록하지않으면 업로드가 이루어지지않음.
        Pair<String, String> fileNameAndExt = createNewFileName(originalFileName);
        String newFileName = fileNameAndExt.getFirst();
        String ext = fileNameAndExt.getSecond();
        String createdName = newFileName + "." + ext;

        contentType = switch (ext) {
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            default -> throw new IllegalStateException("지원하지않는 파일형식");
        };

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3.putObject(new PutObjectRequest(bucket, "image/item" + createdName
                    , multipartFile.getInputStream(), metadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }

        return createdName;
    }

//    private String changeFileNmae(String originFileName, String targeName) {
//        int posOrigin = originFileName.lastIndexOf(".");
//        String fileName = originFileName.substring(0, posOrigin);
//
//        int posTarget = targeName.lastIndexOf(".");
//        String ext = targeName.substring(posTarget + 1);
//        return fileName + "." + ext;
//    }


    private Pair<String, String> createNewFileName(String originFileName) {
        int pos = originFileName.lastIndexOf(".");
        String ext = originFileName.substring(pos + 1);
        String uuid = UUID.randomUUID().toString();
        return Pair.of(uuid, ext);
    }


}
