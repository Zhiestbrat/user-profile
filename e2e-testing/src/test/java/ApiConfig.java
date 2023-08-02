import com.iprody.userprofile.e2e.api.UserProfilesApi;
import com.iprody.userprofile.e2e.invoker.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class ApiConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApiClient apiClient(RestTemplate restTemplate) {
        return new ApiClient(restTemplate);
    }
    @Bean
    public UserProfilesApi userProfileApi(ApiClient apiClient){
        return new UserProfilesApi(apiClient);
    }
}
