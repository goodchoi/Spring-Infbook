package infbook.infbook.domain.member.service;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.dto.MemberSignupDto;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.repository.ShoppingCartRepository;
import infbook.infbook.exception.DuplicatedMemberException;
import jakarta.websocket.Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BCryptPasswordEncoder encoder;

    public Member join(MemberSignupDto memberDto) {

        memberDto.setPassword(encoder.encode(memberDto.getPassword()));
        Member createdMember = Member.getSumbittedMember(memberDto);
        duplicationCheckBeforeSave(createdMember.getAccountId());
        memberRepository.save(createdMember);
        shoppingCartRepository.save(ShoppingCart.builder().member(createdMember).build());

        return createdMember;
    }


    /**
     * ajax Id 중복검사 비즈니스 로직 메서드
     * @Param accountId - 중복 검사 하고자 하는 id
     * @Return 00 - 중복 되는 Id 있음. 01- 정상
     */
    public String validateDuplicatedMember(String accountId) {
        int count = memberRepository.countByAccountIdEquals(accountId);
        if (count > 0) {
            return "00";
        }
        return "01";
    }

    /**
     * 가입하는 사용자의 정보를 입력받아 데이터베이스에 저장하기전, 아이디 중복을 검사하는 메서드
     *
     * @Param accountId 사용자가 입력한 아이디
     * @throw IllegalStateException 입력한 아이디가 이미 존재하는 경우 예외발생
     */
    private void duplicationCheckBeforeSave(String accountId) {
        if (validateDuplicatedMember(accountId).equals("00")) {
            throw new IllegalStateException("[MemberSerivce] 회원 가입 요청시 아이디 중복이 발생했습니다.");
        }
    }

}
