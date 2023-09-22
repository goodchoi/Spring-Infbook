package infbook.infbook.domain.member.controller;

import infbook.infbook.domain.item.service.ItemService;
import infbook.infbook.domain.member.dto.MemberSignupDto;
import infbook.infbook.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/member/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "message", required = false) String message,
                            Model model
    ) {

        model.addAttribute("error", error);
        model.addAttribute("message", message);
        return "login/login";
    }

    @GetMapping("/member/signup")
    public String signupform(Model model) {
        model.addAttribute("member", MemberSignupDto.builder().build());
        return "login/signup";
    }

    @PostMapping("/member/signup")
    public String signup(@Validated @ModelAttribute("member") MemberSignupDto memberSignupDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/login/signup";
        }
        memberService.join(memberSignupDto);

        return "redirect:/login";
    }

    @ResponseBody
    @GetMapping("/member/check/{id}")
    public String signup(@PathVariable(name = "id") String id) {
        return memberService.validateDuplicatedMember(id);
    }
}
