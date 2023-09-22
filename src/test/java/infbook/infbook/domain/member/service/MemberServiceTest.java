package infbook.infbook.domain.member.service;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.domain.UserLevel;
import infbook.infbook.domain.member.dto.MemberSignupDto;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.domain.model.Address;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.repository.ShoppingCartRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ShoppingCartRepository shoppingCartRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("중복된 id로 인한 회원가입 실패")
    @Test
    void join_failed() {
        given(memberRepository.countByAccountIdEquals(anyString()))
                .willReturn(1); // 이미 해당 아이디가 제출하는 상황을 가정


        assertThatThrownBy(() ->memberService.join(buildMemberSignUpDto()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복");
    }

    @DisplayName("회원가입 성공")
    @Test
    void join_success() {

        MemberSignupDto dto = buildMemberSignUpDto();
        Member testMember = buildMember();
        ReflectionTestUtils.setField(testMember,"id",1L);

        given(memberRepository.save(any(Member.class)))
                .willReturn(testMember);

        given(shoppingCartRepository.save(any(ShoppingCart.class)))
                .willReturn(ShoppingCart.builder().member(testMember).build());

        ArgumentCaptor<Member> memberCaptor =
                ArgumentCaptor.forClass(Member.class); //memberRepository.save시 파라미터 Member 캡쳐
        ArgumentCaptor<ShoppingCart> shoppingCartcaptor =
                ArgumentCaptor.forClass(ShoppingCart.class); //shoppingCartRepository.save시 파라미터 Shoppingcart 캡쳐

        //when
        Long joinId = memberService.join(dto);

        //then
        verify(memberRepository,times(1))
                .save(memberCaptor.capture());
        verify(shoppingCartRepository,times(1))
                .save(shoppingCartcaptor.capture());

        Member savedMember = memberCaptor.getValue();
        ShoppingCart savedShoppingCart = shoppingCartcaptor.getValue();


        assertThat("최동현").isEqualTo(savedMember.getName()); //리포지토리에 넘긴 파라미터(멤버)의 이름 일치 확인
        assertThat(savedShoppingCart.getMember().getName()).isEqualTo(savedMember.getName());
        //리포지토리에 넘긴 파라미터(쇼핑카트)의 멤버 필드의 이름 일치 확인
        assertThat(joinId).isEqualTo(1L); //memberService.join 메서드 반환값이 저장된 회원의 id 값과 일치하는지 확인

    }


    private Member buildMember() {
        return Member.builder()
                .name("최동현")
                .address(Address.builder()
                        .street("화곡로 344")
                        .zipcode("2323")
                        .city("서울")
                        .detailedAddress("아크로포레스트 308호").build())
                .accountId("jangu3395")
                .password("@13dfdff")
                .email("jangj3384@naver.com")
                .birthDate(LocalDate.of(1993, 3, 22))
                .telephone("010-2222-3333")
                .userLevel(UserLevel.ROLE_USER)
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
}