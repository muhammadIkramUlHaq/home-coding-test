package dev.mikram.homeassignment.userswithpet.client;

import dev.mikram.homeassignment.common.error.DownstreamApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Component
public class RandomUserClient {

    private static final Duration TIMEOUT = Duration.ofSeconds(8);
    private final WebClient webClient;

    public RandomUserClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @SuppressWarnings("rawtypes")
    public Map fetchUsers(int results, String nat, String seed) {
        try {
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
                    .onStatus(
                            status -> status.isError(),
                            res -> res.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .flatMap(body -> Mono.error(new DownstreamApiException(
                                            "RandomUser API failed (" + res.statusCode().value() + "): "
                                                    + safeBody(body)))))
                    .bodyToMono(Map.class)
                    .timeout(TIMEOUT)
                    .block();
        } catch (WebClientResponseException exception) {
            throw new DownstreamApiException("RandomUser API failed (" + exception.getStatusCode().value() + "): "
                    + safeBody(exception.getResponseBodyAsString()), exception);
        } catch (Exception exception) {
            throw new DownstreamApiException("RandomUser API request failed: " + exception.getMessage(), exception);
        }
    }

    private static String safeBody(String body) {
        if (body == null)
            return "";
        body = body.trim();
        return body.length() > 180 ? body.substring(0, 180) + "..." : body;
    }
}
