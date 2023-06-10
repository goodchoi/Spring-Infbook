package infbook.infbook.domain.member.domain;

import infbook.infbook.domain.model.Address;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 지연로딩 전략을 사용하면 프록시를 이용하므로 없으면 컴파일 오류 발생.
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String accountId;

    private String password;

    private String email;

    private LocalDate birthDate;

    @Embedded
    private Address address;

    private String telephone;

    @Builder //영속성 컨텍스트에서 엔티티가 flush 되기 전까지 Id값은 Null 인 상태를 유지할 수 있어야하므로 Id 값을 제외한 필드들을 대상으로 builder 패턴적용
    public Member(String name, String accountId, String password, String email, LocalDate birthDate, Address address, String telephone) {
        this.name = name;
        this.accountId = accountId;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.telephone = telephone;
    }
}
