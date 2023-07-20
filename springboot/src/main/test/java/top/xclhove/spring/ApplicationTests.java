package top.xclhove.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.xclhove.spring.common.Constant;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(Constant.TEST_MESSAGE);
    }

}
