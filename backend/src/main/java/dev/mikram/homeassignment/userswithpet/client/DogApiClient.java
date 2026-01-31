package dev.mikram.homeassignment.userswithpet.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class DogApiClient {

    private final WebClient webClient;

    public DogApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @SuppressWarnings("rawtypes")
    public Map fetchDogImages(int count) {
        return webClient.get()
                .uri("https://dog.ceo/api/breeds/image/random/" + count)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
