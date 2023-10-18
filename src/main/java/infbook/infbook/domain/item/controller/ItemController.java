package infbook.infbook.domain.item.controller;


import infbook.infbook.domain.item.dto.ItemListDto;
import infbook.infbook.domain.item.dto.ItemSingleDto;
import infbook.infbook.domain.item.service.ItemService;
import infbook.infbook.global.util.CategoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 만약 pathVariable인 상위 카테고리이름과, 하위 카테고리 가 존재하지 않는다면 404 에러 발생시키자.
     */
    private final List<String> sortList = List.of("newest","popular","highprice","lowprice");


    @GetMapping(value = "/item/category/{category}")
    public String getItemPageByCategory(@PathVariable(name = "category") String categoryName,
                           @PageableDefault(sort = "newest",size = 5)Pageable pageable,
                           Model model) {

        if(!CategoryUtil.hasCategory(categoryName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 카테고리입니다.");
        }

        String categoryKorName = CategoryUtil.getKorCategoryName(categoryName);
        String sortingProperty = getSortingProperty(pageable);
        Page<ItemListDto> page;
        if (sortingProperty.equals("popular")) {
            page = itemService.getPopularItemOfCategeory(categoryName,pageable);
        } else {
            page = itemService.getItemOfCategory(categoryName,pageable);

        }
        model.addAttribute("sortList",sortList);
        model.addAttribute("pageSort",sortingProperty);
        model.addAttribute("page",page);
        model.addAttribute("categories",CategoryUtil.getCategoryList());
        model.addAttribute("categoryKorName",categoryKorName);

        return "item/itemcategorylist";
    }


    @GetMapping(value = "/item/category/{category}/{subcategory}")
    public String getItemPageBySubCategory(@PathVariable(name = "category") String categoryName,
                           @PathVariable(name = "subcategory") String subcategoryName,
                           @PageableDefault(sort = "newest",size = 5)Pageable pageable,
                           Model model) {

        if(!CategoryUtil.hasCategory(categoryName) || !CategoryUtil.hasSubCategory(subcategoryName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 카테고리입니다.");
        }

        String categoryKorName = CategoryUtil.getKorCategoryName(categoryName);
        String subcategoryKorName = CategoryUtil.getKorSubCategoryName(subcategoryName);

        String sortingProperty = getSortingProperty(pageable);
        Page<ItemListDto> page;
        if (sortingProperty.equals("popular")) {
            page = itemService.getPopularItemOfSubCategeory(subcategoryName,pageable);
        } else {
            page = itemService.getItemOfSubCategory(subcategoryName,pageable);

        }
        model.addAttribute("sortList",sortList);
        model.addAttribute("pageSort",sortingProperty);
        model.addAttribute("page",page);
        model.addAttribute("categories",CategoryUtil.getCategoryList());
        model.addAttribute("categoryKorName",categoryKorName);
        model.addAttribute("subcategoryKorName",subcategoryKorName);

        return "item/itemsubcategorylist";
    }

    @GetMapping(value = "item/{id}")
    public String getItemById(@PathVariable("id") Long id,Model model) {
        ItemSingleDto findItem = itemService.getItemOfId(id);

        model.addAttribute("item",findItem);
        return "item/item";
    }

    private String getSortingProperty(Pageable pageable) {
        return pageable.getSort().get().findFirst()
                .orElse(Sort.Order.by("newest")).getProperty();
    }

    @GetMapping(value = "/item/search")
    public String searchItem(@ModelAttribute(value = "keyword") String keyword, @PageableDefault(sort = "newest",size = 5)Pageable pageable, Model model){

        String sortingProperty = getSortingProperty(pageable);
        Page<ItemListDto> page;
        if (sortingProperty.equals("popular")) {
            page = itemService.getPopularItemOfKeyword(keyword,pageable);
        } else {
            page = itemService.getItemOfKeyword(keyword,pageable);
        }

        model.addAttribute("sortList",sortList);
        model.addAttribute("pageSort",sortingProperty);
        model.addAttribute("page",page);

        return "item/itemsearch";
    }
}
