package infbook.infbook.domain.member.service;

import infbook.infbook.abstractTest.ServiceTest;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.domain.Oauth2Provider;
import infbook.infbook.domain.member.domain.UserLevel;
import infbook.infbook.domain.member.dto.MemberSignupDto;
import infbook.infbook.domain.member.dto.MemberUpdateInfo;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest extends ServiceTest {

    @InjectMocks
    MemberService memberService;


    @DisplayName("계정 ID가 중복일 시 반환 값 '00'을 확인 할 수 있다.")
    @Test
    void duplicatie_check_duplicated() {
        //given
        given(memberRepository.countByAccountIdEquals(any(String.class)))
                .willReturn(1);
        //when
        String result = memberService.validateDuplicatedMember("test");

        //then
        assertThat(result).isEqualTo("00");
    }

    @DisplayName("계정 ID가 중복이 아닐 시 반환 값 '01'을 확인 할 수 있다.")
    @Test
    void duplicatie_check_unique() {
        //given
        given(memberRepository.countByAccountIdEquals(any(String.class)))
                .willReturn(0);
        //when
        String result = memberService.validateDuplicatedMember("test");

        //then
        assertThat(result).isEqualTo("01");
    }


    @DisplayName("중복된 id로 인한 회원가입 실패")
    @Test
    void join_failed() {
        given(memberRepository.countByAccountIdEquals(anyString()))
                .willReturn(1); // 이미 해당 아이디가 제출하는 상황을 가정


        assertThatThrownBy(() -> memberService.join(buildMemberSignUpDto()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복");
    }

    @DisplayName("중복된 계정 id가 없다면 회원가입을 할 수 있다.")
    @Test
    void join_success() {
        MemberSignupDto dto = buildMemberSignUpDto();
        given(memberRepository.countByAccountIdEquals(any(String.class))).willReturn(0);
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(shoppingCartRepository.save(any(ShoppingCart.class)))
                .willReturn(shoppingCart);
        //when
        memberService.join(dto);

        then(memberRepository).should().save(any(Member.class));
        then(shoppingCartRepository).should().save(any(ShoppingCart.class));
    }

    @DisplayName("OAuth 신규 가입자에 대하여 추가정보를 업데이트 할 수 있다.")
    @Test
    void updateOauthUserInfo() {
        Member createdMember = buildMember();
        ReflectionTestUtils.setField(createdMember,"id",1L);

        //given
        given(memberRepository.findById(any(Long.class))).willReturn(Optional.of(createdMember));

        Member updatedMember = memberService.updateOauthUserInfo(1L, memberUpdateInfo());
        //then
        assertThat(updatedMember.getUserLevel()).isEqualTo(UserLevel.ROLE_USER);
    }

    private Member buildMember() {
        return Member.builder()
                .oAuth2Id("123213333")
                .email("jangu3394@nvaer.com")
                .provider(Oauth2Provider.KAKAO)
                .userLevel(UserLevel.ROLE_ANONYMOUS)
                .build();
    }

    private MemberSignupDto buildMemberSignUpDto() {
        return MemberSignupDto.builder()
                .name("최동현")
                .street("화곡로 344")
                .zipcode("2323")
                .city("서울")
                .detailedAddress("아크로포레스트 308호")
                .accountId("jangu3395")
                .password("@13dfdff")
                .email("jangj3384@naver.com")
                .birthDate(LocalDate.of(1993, 3, 22))
                .telephone("010-2222-3333")
                .build();
    }

    private MemberUpdateInfo memberUpdateInfo() {
        return MemberUpdateInfo.builder()
                .name("최동현")
                .street("화곡로 344")
                .zipcode("2323")
                .city("서울")
                .detailedAddress("아크로포레스트 308호")
                .birthDate(LocalDate.of(1993, 3, 22))
                .telephone("010-2222-3333")
                .build();
    }


}