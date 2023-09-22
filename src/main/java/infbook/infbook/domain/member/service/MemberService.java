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

    public Long join(MemberSignupDto memberDto) {

        memberDto.setPassword(encoder.encode(memberDto.getPassword()));
        Member sumbittedMember = Member.getSumbittedMember(memberDto);
        duplicationCheckBeforeSave(sumbittedMember.getAccountId());
        Member savedMember = memberRepository.save(sumbittedMember);
        shoppingCartRepository.save(ShoppingCart.builder().member(sumbittedMember).build());

        return savedMember.getId();
    }

    public String validateDuplicatedMember(String id) {
        int count = memberRepository.countByAccountIdEquals(id);
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
        if (memberRepository.countByAccountIdEquals(accountId) > 0) {
            throw new IllegalStateException("[MemberSerivce] 회원 가입 요청시 아이디 중복이 발생했습니다.");
        }
    }

}
