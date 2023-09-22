package infbook.infbook.domain.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTest {


    @Test
    void telephoneRegExTest() {
        String regEx ="^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})";

        Assertions.assertThat("010-2222-3334").matches(regEx);
    }

    @Test
    void telephoneRegExFailTest() {
        String regEx ="^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})";

        Assertions.assertThat("010-222-222").doesNotMatch(regEx);
    }

}
