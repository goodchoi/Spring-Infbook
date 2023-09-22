package infbook.infbook.domain.member.service;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.dto.CustomUserDetails;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.domain.shoppingcart.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Member> optionalFindMember = memberRepository.findByAccountId(userId);

        Member findMember = optionalFindMember.orElseThrow(() -> new UsernameNotFoundException("id없음."));

        CustomUserDetails details = new CustomUserDetails(findMember);
        return details;
    }


}
