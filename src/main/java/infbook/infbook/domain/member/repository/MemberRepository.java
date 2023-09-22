package infbook.infbook.domain.member.repository;

import infbook.infbook.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long> {

    int countByAccountIdEquals(String accountId);

    Optional<Member> findByAccountId(String accountId);
}
