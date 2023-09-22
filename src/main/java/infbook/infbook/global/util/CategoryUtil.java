package infbook.infbook.global.util;

import infbook.infbook.domain.category.domain.Category;
import infbook.infbook.domain.category.domain.SubCategory;
import infbook.infbook.domain.category.repository.CategoryRepository;
import infbook.infbook.domain.category.repository.SubCategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public class CategoryUtil {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private static List<Category> categoryList;
    private static Map<String, String> categoryNameMap;
    private static Map<String,String> subcategoryNameMap;


    @PostConstruct
    private void init() {
        categoryList = categoryRepository.findFetchAll();
        setNameMap(categoryList);
    }

    private void setNameMap(List<Category> catgories) {
        categoryNameMap = catgories.stream()
                .collect(Collectors.toMap(Category::getEngName,Category::getName));
        subcategoryNameMap = catgories.stream().flatMap(c -> c.getChildren().stream())
                .distinct()
                .collect(Collectors.toMap(SubCategory::getEngName,SubCategory::getName));
    }

    public static List<Category> getCategoryList() {
        return Collections.unmodifiableList(categoryList);
    }

    public static boolean hasCategory(String categoryName) {
        return categoryNameMap.containsKey(categoryName);
    }

    public static boolean hasSubCategory(String subCategoryName) {
        return subcategoryNameMap.containsKey(subCategoryName);
    }

    public static String getKorCategoryName(String categoryName) {
        return categoryNameMap.get(categoryName);
    }

    public static String getKorSubCategoryName(String subCategoryName) {
        return subcategoryNameMap.get(subCategoryName);
    }

    public static void addCategory(Category category) {

    }


}
