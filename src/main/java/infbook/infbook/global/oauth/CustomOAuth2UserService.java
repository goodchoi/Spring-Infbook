package infbook.infbook.global.oauth;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.domain.Oauth2Provider;
import infbook.infbook.domain.member.dto.CustomUserDetails;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);
        return getOwnOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    protected OAuth2User getOwnOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

        Oauth2Provider authProvider = Oauth2Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OauthInfo oAuth2UserInfo = OauthInfoFactory.getOauthInfo(authProvider, oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new RuntimeException("Oauth2 email을 확보하지못했습니다.");
        }

        Member member = memberRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        //이미 가입된 경우
        if (member != null) {
            if (ObjectUtils.nullSafeEquals(member.getProvider(), authProvider)) {
                member.updateOauthInfo(authProvider, oAuth2UserInfo.getId());
            }
        }
        //신규 가입자의 경우 현재 주어진 정보만 가지고 불완전한 멤버엔티티 생성 후 저장.
        else {
            member = memberRepository.save(Member.createOauthMember(oAuth2UserInfo, authProvider));
            shoppingCartRepository.save(ShoppingCart.builder().member(member).build());
        }
        return CustomUserDetails.createOauthUserDetails(member, oAuth2UserInfo.getAttributes());
    }

}
