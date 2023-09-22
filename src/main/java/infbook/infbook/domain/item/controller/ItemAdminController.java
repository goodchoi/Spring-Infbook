package infbook.infbook.domain.item.controller;

import infbook.infbook.domain.item.domain.*;
import infbook.infbook.domain.item.dto.ItemAdminDto;
import infbook.infbook.domain.item.dto.ItemSaveDto;
import infbook.infbook.domain.item.dto.ItemUpdateDto;
import infbook.infbook.domain.item.dto.ItemUpdateFormDto;
import infbook.infbook.domain.item.service.ItemAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class ItemAdminController {

    private final ItemAdminService itemService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String adminHome() {
        return "/admin/adminhome";
    }

    @RequestMapping(value = "/item/insert", method = RequestMethod.GET)
    public String itemInsertForm(Model model) {
        model.addAttribute("item", ItemSaveDto.builder().
                subCategories(new ArrayList<>())
                .build());
        model.addAttribute("categoryList", itemService.getCategoryList());
        return "/admin/createbookform";
    }

    @RequestMapping(value = "/item/insert", method = RequestMethod.POST)
    public String itemInsert(@Validated @ModelAttribute(name = "item") ItemSaveDto itemSaveDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryList", itemService.getCategoryList());
            return "/admin/createbookform";
        }
        Item savedItem = itemService.enrollItem(itemSaveDto);

        redirectAttributes.addAttribute("id", savedItem.getId());

        return "redirect:/admin/item/{id}";
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    public String getItem(@PathVariable(name = "id") Long itemId, Model model) {

        ItemAdminDto item = itemService.getItemAdiminDtoByItemId(itemId);
        model.addAttribute("item", item);

        return "/admin/book";
    }

    /**
     * 수정은 정책적으로
     * 이미지,가격,수량,카테고리,하위카테고리로 제한한다.
     *
     * 아이템 수정시 주의할 점
     * 1. 하위 카테고리 변경시 새로운 하위카테고리 - 아이템 엔티티객체 생성 해야함. 변경된 기존 하위카테고리 - 아이템 엔티티객체 삭제
     * 2.
     */
    @RequestMapping(value = "/item/{id}/edit", method = RequestMethod.GET)
    public String editItemForm(@PathVariable(name = "id") Long itemId, Model model) {

        ItemUpdateFormDto item = itemService.getItemSaveDtoByItemId(itemId);
        model.addAttribute("item", item);
        model.addAttribute("categoryList", itemService.getCategoryList());

        return "/admin/updatebookform";
    }

    @RequestMapping(value = "/item/{id}/edit", method = RequestMethod.POST)
    public String editItem(@PathVariable(name = "id") Long itemId, @ModelAttribute("item") ItemUpdateDto itemUpdateDto,
                           BindingResult bindingResult, Model model) throws IOException {


        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryList", itemService.getCategoryList());
            return "/admin/createbookform";
        }

        Long updateId = itemService.updateItem(itemId, itemUpdateDto);

        return "redirect:/admin/item/{id}";
    }

}
