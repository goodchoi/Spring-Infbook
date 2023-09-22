package infbook.infbook.domain.member.repository;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.domain.UserLevel;
import infbook.infbook.domain.model.Address;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;


@Slf4j
@DataJpaTest
class MemberRepositoryTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    EntityManager em;
//
//    @Test
//    void createMember() {
//        Member createMember = memberRepository.save(
//                buildMember()
//        );
//        log.info("{}",createMember);
//    }
//
//
//    @Test
//    void validateDuplicatedMember() {
//        //given
//        Member newMember = buildMember();
//
//        //when
//        memberRepository.save(newMember);
//
//        em.flush();
//        em.clear();
//
//        Assertions.assertThat(memberRepository.countByAccountIdEquals(newMember.getAccountId())).isEqualTo(1);
//    }
//
//
//    private Member buildMember() {
//        return Member.builder()
//                .name("최동현")
//                .address(Address.builder()
//                        .street("화곡로 344")
//                        .zipcode("2323")
//                        .city("서울")
//                        .detailedAddress("아크로포레스트 308호").build())
//                .accountId("jangu3395")
//                .password("@13dfdff")
//                .email("jangj3384@naver.com")
//                .birthDate(LocalDate.of(1993, 3, 22))
//                .telephone("010-2222-3333")
//                .userLevel(UserLevel.USER)
//                .build();
//    }


}