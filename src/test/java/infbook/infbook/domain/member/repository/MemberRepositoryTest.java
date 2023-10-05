package infbook.infbook.domain.member.repository;

import infbook.infbook.abstractTest.RepostoryTest;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.domain.UserLevel;
import infbook.infbook.domain.model.Address;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
class MemberRepositoryTest extends RepostoryTest {

    @DisplayName("id에대한 중복여부를 데이터베이스로부터 확인할 수있다.")
    @Test
    void validateDuplicatedMember() {

        int result = memberRepository.countByAccountIdEquals(savedMember.getAccountId());

        assertThat(result).isEqualTo(1);
    }

}