package stepdefs;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class HealthcheckStep {

    private final RestTemplate restTemplate;

    @Given("UP Service is up and running")
    public void upServiceIsUpAndRunning() {
        ResponseEntity<String> healthResponse = restTemplate.getForEntity("http://localhost:8088/actuator/health", String.class);
        assertThat(healthResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String healthResponseBody = healthResponse.getBody();
        assertThat(healthResponseBody).isNotNull();
        assertThat(healthResponseBody).contains("\"status\":\"UP\"");
    }

    @And("User Endpoint is available:")
    public void userEndpointIsAvailable(Map<String, String> expectedUserEndpoint) {
//        ResponseEntity<String> mappingResponseEntity = restTemplate.getForEntity("http://localhost:8088/actuator/mappings", String.class);
//        assertThat(mappingResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        String mappingResponse = mappingResponseEntity.getBody();
//        assertThat(mappingResponse).isNotNull();
//        assertThat(mappingResponse).contains("/users");
    }
}
