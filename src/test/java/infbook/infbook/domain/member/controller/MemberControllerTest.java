package infbook.infbook.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import infbook.infbook.abstractUtils.ControllerUtil;
import infbook.infbook.abstractUtils.RepostoryUtil;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.dto.MemberSignupDto;
import infbook.infbook.domain.member.service.MemberService;
import infbook.infbook.domain.model.Address;
import infbook.infbook.global.config.security.SecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MemberControllerTest extends ControllerUtil {

    @Autowired
    protected ObjectMapper objectMapper;


    @Test
    void signup() throws Exception {

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("name", "");
        data.add("accountId", "asdf");
        data.add("password", "adsf");
        data.add("city", "aa");
        data.add("zipcode", "adsf");
        data.add("detailedAddress", "adsf");
        data.add("email", "adsf");
        data.add("street", "adsf");
        data.add("birthDate", "2033.3.2");

        mockMvc.perform(post("/member/signup")
                .params(data)
        ).andExpect(status().isOk());
    }


}