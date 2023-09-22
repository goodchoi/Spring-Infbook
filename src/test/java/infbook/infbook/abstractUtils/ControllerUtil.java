package infbook.infbook.abstractUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.domain.UserLevel;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.utils.AutoConfigureMockMvcWithEncoding;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static infbook.infbook.utils.models.*;

@Slf4j
@SpringBootTest()
@Transactional
@AutoConfigureMockMvcWithEncoding
@ActiveProfiles("test")
public abstract class ControllerUtil {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    protected final String savedMemberId = "test";

    @BeforeEach
    void setupLogin() {
        Member savedMember = Member.builder()
                .accountId(savedMemberId).password("123")
                .name(MEMBER_NAME)
                .birthDate(MEMBER_BIRTHDATE)
                .email(MEMBER_EMAIL)
                .telephone(MEMBER_TELEPHONE)
                .address(ADDRESS)
                .userLevel(MEMBER_USERLEVEL)
                .build();

        memberRepository.save(savedMember);
    }

}
