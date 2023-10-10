package infbook.infbook.domain.member.domain;

import infbook.infbook.domain.member.dto.MemberSignupDto;
import infbook.infbook.domain.member.dto.MemberUpdateInfo;
import infbook.infbook.domain.model.Address;
import infbook.infbook.global.common.BaseEntity;
import infbook.infbook.global.oauth.OauthInfo;
import jakarta.persistence.*;
import jakarta.websocket.Encoder;
import lombok.*;
import org.springframework.context.MessageSource;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(columnDefinition = "varchar(60)")
    private String name;

    @Column(columnDefinition = "varchar(60)")
    private String accountId;

    @Column(columnDefinition = "varchar(100)")
    private String password;

    @Column(nullable = false, columnDefinition = "varchar(50)")
    private String email;

    @Column(columnDefinition = "datetime")
    private LocalDate birthDate;

    @Embedded
    private Address address;

    @Column(columnDefinition = "varchar(15)")
    private String telephone;

    @Column(columnDefinition = "varchar(255)")
    private String oAuth2Id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    private Oauth2Provider provider;


    @Enumerated(value = EnumType.STRING)
    @Column
    private UserLevel userLevel;

    @Builder //영속성 컨텍스트에서 엔티티가 flush 되기 전까지 Id값은 Null 인 상태를 유지할 수 있어야하므로 Id 값을 제외한 필드들을 대상으로 builder 패턴적용
    public Member(String name, String accountId, String password, String email, LocalDate birthDate, Address address, String telephone, String oAuth2Id, Oauth2Provider provider, UserLevel userLevel) {
        this.name = name;
        this.accountId = accountId;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.telephone = telephone;
        this.provider = provider;
        this.oAuth2Id = oAuth2Id;
        this.userLevel = userLevel;
    }

    public static Member createOauthMember(OauthInfo oauthInfo,Oauth2Provider oauth2Provider){
        return Member.builder()
                .accountId(UUID.randomUUID().toString().substring(0,7))
                .password(UUID.randomUUID().toString())
                .email(oauthInfo.getEmail())
                .oAuth2Id(oauthInfo.getId())
                .provider(oauth2Provider)
                .userLevel(UserLevel.ROLE_ANONYMOUS)
                .build();
    }

    public static Member getSumbittedMember(MemberSignupDto dto) {
        return Member.builder()
                .name(dto.getName())
                .accountId(dto.getAccountId())
                .password(dto.getPassword())
                .birthDate(dto.getBirthDate())
                .telephone(dto.getTelephone())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .userLevel(UserLevel.ROLE_USER)
                .build();
    }

    public void updateOauthInfo(Oauth2Provider provider,String oAuth2Id){
        this.provider = provider;
        this.oAuth2Id = oAuth2Id;
    }

    public void updateAdditionalInfo(MemberUpdateInfo memberUpdateInfo){
        this.name = memberUpdateInfo.getName();
        this.address = memberUpdateInfo.getAddress();
        this.birthDate = memberUpdateInfo.getBirthDate();
        this.telephone = memberUpdateInfo.getTelephone();
        this.userLevel = UserLevel.ROLE_USER;
    }

}
