package stepdefs;

import com.iprody.userprofile.e2e.api.UserProfilesApi;
import com.iprody.userprofile.e2e.invoker.ApiClient;
import com.iprody.userprofile.e2e.model.UserDetailsRequest;
import com.iprody.userprofile.e2e.model.UserRequest;
import com.iprody.userprofile.e2e.model.UserResponse;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateUserStep {

    private ResponseEntity<UserResponse> response;
    private final UserProfilesApi api;
    private ResponseEntity<String> error;

    public CreateUserStep(UserProfilesApi api) {
        this.api = api;
    }


    @When("a client want create user with mandatory parameters:")
    public void aClientWantsCreateUserWithParameters(Map<String, String> userData) {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName(userData.get("firstName"));
        userRequest.setLastName(userData.get("lastName"));
        userRequest.setEmail(userData.get("email"));
        try {
            response = api.addUserWithHttpInfo(userRequest);
        } catch (HttpStatusCodeException ex) {
            error = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }

    }

    @When("the client updates user details with mandatory info:")
    public void theClientUpdatesUserDetailsWithMandatoryInfo(Map<String, String> userData) {
        UserDetailsRequest userDetailsRequest = new UserDetailsRequest();
        userDetailsRequest.setMobilePhone(userData.get("telegramId"));
        userDetailsRequest.setTelegramId(userData.get("mobilePhone"));
        try {
            response = api.updateUserDetailsWithHttpInfo(response.getBody().getId(), userDetailsRequest);
        } catch (HttpStatusCodeException ex) {
            error = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }

    }
    @And("response body contains:")
    public void responseBodyContains(Map<String, String> expectedData) {
        UserResponse userResponse = response.getBody();
        assertThat(userResponse.getFirstName()).isEqualTo(expectedData.get("firstName"));
        assertThat(userResponse.getLastName()).isEqualTo(expectedData.get("lastName"));
        assertThat(userResponse.getEmail()).isEqualTo(expectedData.get("email"));
        assertThat(userResponse.getTelegramId()).isEqualTo(expectedData.get("telegramId"));
        assertThat(userResponse.getMobilePhone()).isEqualTo(expectedData.get("mobilePhone"));
    }
    @When("a client wants to update a user")
    public void wantsToUpdateUserTo(Map<String, String> userData) {
        UserResponse user = response.getBody();
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName(userData.get("firstName"));
        userRequest.setLastName(userData.get("lastName"));
        userRequest.setEmail(userData.get("email"));
        response = api.updateUserWithHttpInfo(user.getId(), userRequest);
    }
    @Then("response is successful code {int}")
    public void responseCodeIs201(int status) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Then("response is request error code {int}")
    public void responseCodeIs(int status) {
        assertThat(error.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @And("response body contains error:")
    public void responseBodyContainsError() {
        assertThat(error.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(error.getBody()).isEqualTo("Validation Error");
    }

    @Then("response is server error code {int}")
    public void responseIsServerErrorCode(int status) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
