package infbook.infbook.domain.member.controller;

import infbook.infbook.abstractTest.ControllerTest;

import infbook.infbook.global.jwt.JwtProperties;
import infbook.infbook.global.jwt.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

import static infbook.infbook.utils.models.MEMBER_PASSWORD;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class MemberControllerTest extends ControllerTest {


    @DisplayName("BindingResult 동작확인 - 이름 미입력시 Model에 Error 추가 후 다시 회원가입  페이지로 이동")
    @Test
    void signup_field_error() throws Exception {

        MultiValueMap<String, String> imperfectMemberSignUpForm = getPerfectMemberSignUpForm();

        imperfectMemberSignUpForm.set("name", ""); //이름을 기입하지 않은 경우를 모방


        mockMvc.perform(post("/member/signup")
                        .params(imperfectMemberSignUpForm)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(csrf())
                )
                .andExpect(model().attributeHasFieldErrors("member", "name")) //Binding 된 객체, 필드명
                .andExpect(status().isOk());
        // .andDo(print());
    }

    @DisplayName("회원가입 성공 - 회원가입성공시 로그인화면으로 리다이렉트 되며 , DB에 성공적으로 저장된다.")
    @Test
    void signup_success() throws Exception {

        MultiValueMap<String, String> perfectMemberSignUpForm = getPerfectMemberSignUpForm();


        mockMvc.perform(post("/member/signup")
                        .params(perfectMemberSignUpForm)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(csrf())
                )
                .andExpect(redirectedUrl("/member/login"));

        assertTrue((memberRepository.findByAccountId("safff123").isPresent()));
    }

    @DisplayName("ajax 중복검사 요청시 중복일 경우 response의 body에 반환값 '00'을 가진다")
    @Test
    void duplicate_Check() throws Exception {
        mockMvc.perform(get("/member/check/" + savedMember.getAccountId())
                        .accept(MediaType.TEXT_PLAIN)
                )
                .andExpect(result -> {
                    assertEquals("00", result.getResponse().getContentAsString()); //"00" 중복
                });
    }

    @DisplayName("ajax 중복검사 요청시 중복이 아닐 경우 response의 body에 반환값 '01'을 가진다")
    @Test
    void duplicate_Check_Success() throws Exception {
        mockMvc.perform(get("/member/check/" + "anonymous112")
                        .accept(MediaType.TEXT_PLAIN)
                )
                .andExpect(result -> {
                    assertEquals("01", result.getResponse().getContentAsString()); //"01" 성공
                });
    }

    @DisplayName("로그인 성공시 JWT 생성후 쿠키에 등록하고, 홈화면으로 리다이렉트 됨.")
    @Test
    void login_success() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", savedMember.getAccountId())
                        .param("password", MEMBER_PASSWORD)
                        .with(csrf())
                )
                .andExpect(result -> {
                    assertThat(result.getResponse().getCookie(JwtProperties.COOKIE_NAME)).isNotNull()
                            .extracting(cookie -> JwtUtils.getUserId(cookie.getValue())).isEqualTo(savedMember.getId());
                })
                .andExpect(redirectedUrl("/"));
        // 로그인 성공으로 발급된 JWT가 쿠키로 넘어오는 지 확인하고, JWT가 실제 로그인한 유저로 발급된 것인지에대한 유효성확인.

        //.andDo(print());
    }


    private MultiValueMap<String, String> getPerfectMemberSignUpForm() {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("name", "최동현");  //이름을 입력하지 않은 경우
        data.add("accountId", "safff123");
        data.add("password", "asdff!344");
        data.add("telephone", "01092053502");
        data.add("city", "서울");
        data.add("zipcode", "23444");
        data.add("detailedAddress", "309호");
        data.add("email", "jangu3384@gmail.com");
        data.add("street", "테스트");
        data.add("birthDate", "20330302");

        return data;
    }


}