package infbook.infbook.domain.member.dto;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.domain.UserLevel;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {
    private final Member member;
    private Map<String, Object> attributes;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    //Oauth2용 객체 생성
    public static OAuth2User createOauthUserDetails(Member member, Map<String, Object> attributes) {
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        customUserDetails.setAttributes(attributes);
        return customUserDetails;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();

        list.add(new SimpleGrantedAuthority(this.member.getUserLevel().toString()));
        return list;
    }


    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getAccountId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    //Oatuh2User Override!
    @Override
    public String getName() {
        return String.valueOf(this.member.getId());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
