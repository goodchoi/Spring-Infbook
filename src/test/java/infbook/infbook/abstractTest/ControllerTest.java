package infbook.infbook.abstractTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import infbook.infbook.domain.category.repository.CategoryRepository;
import infbook.infbook.domain.category.repository.SubCategoryRepository;
import infbook.infbook.domain.item.repository.ItemAdminRepository;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.domain.Oauth2Provider;
import infbook.infbook.domain.member.domain.UserLevel;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.utils.AutoConfigureMockMvcWithEncoding;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static infbook.infbook.utils.models.*;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureMockMvcWithEncoding
public abstract class ControllerTest {


    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ItemAdminRepository itemAdminRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected SubCategoryRepository subCategoryRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    protected ObjectMapper objectMapper;

    protected final String savedMemberId = "test2133";

    protected Member savedMember;
    protected Member oauthTempMember;


    @BeforeEach
    void setupMember() {
        this.savedMember = Member.builder()
                .accountId(savedMemberId)
                .password(bCryptPasswordEncoder.encode(MEMBER_PASSWORD))
                .name(MEMBER_NAME)
                .birthDate(MEMBER_BIRTHDATE)
                .email(MEMBER_EMAIL)
                .telephone(MEMBER_TELEPHONE)
                .address(ADDRESS)
                .userLevel(MEMBER_USERLEVEL)
                .build();

        this.oauthTempMember = Member.builder()
                .accountId(savedMemberId+"33")
                .password(bCryptPasswordEncoder.encode(MEMBER_PASSWORD)+"33")
                .email(MEMBER_EMAIL+"m")
                .provider(Oauth2Provider.KAKAO)
                .oAuth2Id("12312333")
                .userLevel(UserLevel.ROLE_ANONYMOUS)
                .build();

        memberRepository.save(savedMember);
        memberRepository.save(oauthTempMember);
    }
}
