import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;


@CucumberContextConfiguration
@SpringBootTest(classes = {ApiConfig.class})
public class CucumberSpringContextConfiguration {
}