package infbook.infbook.domain.member.domain;

import infbook.infbook.domain.member.dto.MemberSignupDto;
import infbook.infbook.domain.model.Address;
import infbook.infbook.global.common.BaseEntity;
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

    @Column(nullable = false, columnDefinition = "varchar(60)")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(60)")
    private String accountId;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String password;

    @Column(nullable = false, columnDefinition = "varchar(50)")
    private String email;

    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDate birthDate;

    @Embedded
    private Address address;

    @Column(nullable = false, columnDefinition = "varchar(15)")
    private String telephone;

    @Enumerated(value = EnumType.STRING)
    @Column
    private UserLevel userLevel;


    @Builder //영속성 컨텍스트에서 엔티티가 flush 되기 전까지 Id값은 Null 인 상태를 유지할 수 있어야하므로 Id 값을 제외한 필드들을 대상으로 builder 패턴적용
    public Member(String name, String accountId, String password, String email, LocalDate birthDate, Address address, String telephone, UserLevel userLevel) {
        this.name = name;
        this.accountId = accountId;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.telephone = telephone;
        this.userLevel = userLevel;
    }

    public static Member getSumbittedMember(MemberSignupDto dto) {
        return Member.builder()
                .name(dto.getName())
                .accountId(dto.getAccountId())
                .password(dto.getPassword())
                .birthDate(dto.getBirthDate())
                .telephone(dto.getTelephone())
                .address(Address.builder()
                        .detailedAddress(dto.getDetailedAddress())
                        .city(dto.getCity())
                        .street(dto.getStreet())
                        .zipcode(dto.getZipcode())
                        .build())
                .email(dto.getEmail())
                .userLevel(UserLevel.ROLE_USER)
                .build();
    }

}
