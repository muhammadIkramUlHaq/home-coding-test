package dev.mikram.homeassignment.userswithpet.client;

import dev.mikram.homeassignment.common.error.DownstreamApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Component
public class DogApiClient {

    private static final Duration TIMEOUT = Duration.ofSeconds(8);
    private final WebClient webClient;

    public DogApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @SuppressWarnings("rawtypes")
    public Map fetchDogImages(int count) {
        try {
            return webClient.get()
                    .uri("https://dog.ceo/api/breeds/image/random/" + count)
                    .retrieve()
                    .onStatus(
                            status -> status.isError(),
                            res -> res.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .flatMap(body -> Mono.error(new DownstreamApiException(
                                            "Dog API failed (" + res.statusCode().value() + "): " + safeBody(body)))))
                    .bodyToMono(Map.class)
                    .timeout(TIMEOUT)
                    .block();
        } catch (WebClientResponseException exception) {
            // non-2xx
            throw new DownstreamApiException(
                    "Dog API failed (" + exception.getStatusCode().value() + "): "
                            + safeBody(exception.getResponseBodyAsString()),
                    exception);
        } catch (Exception exception) {
            // connection refused, timeout, DNS, etc.
            throw new DownstreamApiException("Dog API request failed: " + exception.getMessage(), exception);
        }
    }

    private static String safeBody(String body) {
        if (body == null)
            return "";
        body = body.trim();
        return body.length() > 180 ? body.substring(0, 180) + "..." : body;
    }
}
