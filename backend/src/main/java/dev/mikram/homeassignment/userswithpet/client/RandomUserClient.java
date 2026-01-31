package dev.mikram.homeassignment.userswithpet.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class RandomUserClient {

    private final WebClient webClient;

    public RandomUserClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @SuppressWarnings("rawtypes")
    public Map fetchUsers(int results, String nat, String seed) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .scheme("https")
                            .host("randomuser.me")
                            .path("/api/")
                            .queryParam("results", results);

                    if (nat != null && !nat.isBlank()) {
                        uriBuilder.queryParam("nat", nat.trim());
                    }

                    if (seed != null && !seed.isBlank()) {
                        uriBuilder.queryParam("seed", seed.trim());
                    }

                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
