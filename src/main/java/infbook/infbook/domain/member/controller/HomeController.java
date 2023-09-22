package infbook.infbook.domain.member.controller;

import infbook.infbook.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("bestSellers", itemService.getBestSellers());
        return "index";
    }

}
