package infbook.infbook.domain.member.controller;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.dto.CustomUserDetails;
import infbook.infbook.domain.member.dto.MemberSignupDto;
import infbook.infbook.domain.member.dto.MemberUpdateInfo;
import infbook.infbook.domain.member.service.MemberService;
import infbook.infbook.global.jwt.JwtPrincipal;
import infbook.infbook.global.jwt.JwtProperties;
import infbook.infbook.global.jwt.JwtUtils;
import infbook.infbook.global.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static infbook.infbook.global.jwt.JwtProperties.*;


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

    @GetMapping("/member/moreInfo")
    public String moreInfoform(Model model) {
        model.addAttribute("member", MemberUpdateInfo.builder().build());
        return "login/moreinfo";
    }

    @PostMapping("/member/moreInfo")
    public String moreInfo(@Validated @ModelAttribute("member") MemberUpdateInfo memberUpdateInfo,
                           BindingResult bindingResult,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "/login/moreinfo";
        }


        Long userIdFromCooke = JwtUtils.getUserIdFromCooke(request);
        Member updatedMember = memberService.updateOauthUserInfo(userIdFromCooke, memberUpdateInfo);
        String newToken = JwtUtils.createToken(updatedMember);
        CookieUtil.recreateCookie(request, response, newToken, JWT_COOKIE_NAME, JWT_EXPIRATION_TIME);

        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("/member/oauth")
    public String oauthRedirect() {
        return "1";
    }

    @PostMapping("/member/signup")
    public String signup(@Validated @ModelAttribute("member") MemberSignupDto memberSignupDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/login/signup";
        }
        memberService.join(memberSignupDto);

        return "redirect:/member/login";
    }

    @ResponseBody
    @GetMapping("/member/check/{id}")
    public String signup(@PathVariable(name = "id") String id) {
        return memberService.validateDuplicatedMember(id);
    }
}
