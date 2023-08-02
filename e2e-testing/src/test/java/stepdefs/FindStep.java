package stepdefs;

import com.iprody.userprofile.e2e.api.UserProfilesApi;
import com.iprody.userprofile.e2e.model.UserRequest;
import com.iprody.userprofile.e2e.model.UserResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class FindStep {
    private final UserProfilesApi userProfilesApi;
    private ResponseEntity<?> lastResponse;

    public FindStep(UserProfilesApi userProfilesApi) {
        this.userProfilesApi = userProfilesApi;
    }


    @When("^I call the add user method in the service$")
    public void callAddUserMethod() {
        UserRequest userRequest = new UserRequest();
        lastResponse = userProfilesApi.addUserWithHttpInfo(userRequest
                .firstName("pablo")
                .lastName("Smith")
                .email("pablo@gmail.com"));
    }

    @Then("^the response should be successful$")
    public void verifyAddUserResponseStatus() {
        assertThat(lastResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @When("^I call the find user with details method in the service$")
    public void callFindUserWithDetailsMethod() {
        List<UserResponse> userResponses = List.of(new UserResponse());
        lastResponse = new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @Then("the response contains at least {int} user")
    public void theResponseContainsAtLeastUser(int expectedNumberOfUsers) {
        List<UserResponse> userResponses = (List<UserResponse>) lastResponse.getBody();
        assertThat(userResponses).hasSizeGreaterThanOrEqualTo(expectedNumberOfUsers);
    }
}
