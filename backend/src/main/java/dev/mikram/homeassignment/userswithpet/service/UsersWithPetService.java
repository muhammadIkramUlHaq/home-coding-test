package dev.mikram.homeassignment.userswithpet.service;

import dev.mikram.homeassignment.userswithpet.client.DogApiClient;
import dev.mikram.homeassignment.userswithpet.client.RandomUserClient;
import dev.mikram.homeassignment.userswithpet.dto.UserWithPetDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersWithPetService {

    private static final Set<String> ALLOWED_NATS = Set.of(
            "AU", "BR", "CA", "CH", "DE", "DK", "ES", "FI", "FR", "GB", "IE", "IN", "IR", "MX", "NL", "NO", "NZ", "RS",
            "TR", "UA", "US");

    private final RandomUserClient randomUserClient;
    private final DogApiClient dogApiClient;

    public UsersWithPetService(RandomUserClient randomUserClient, DogApiClient dogApiClient) {
        this.randomUserClient = randomUserClient;
        this.dogApiClient = dogApiClient;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<UserWithPetDto> getUsersWithPet(int results, String nat, String seed) {
        int safeResults = clampResults(results);
        String normalizedNat = normalizeAndValidateNat(nat);

        Map randomUserResponse = randomUserClient.fetchUsers(safeResults, normalizedNat, seed);
        List<Map> userResults = (List<Map>) randomUserResponse.get("results");

        Map dogApiResponse = dogApiClient.fetchDogImages(safeResults);
        List<String> dogImageUrls = (List<String>) dogApiResponse.get("message");

        int count = Math.min(userResults.size(), dogImageUrls.size());

        List<UserWithPetDto> userWithPets = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Map user = userResults.get(i);

            Map name = (Map) user.get("name");
            String fullName = name.get("first") + " " + name.get("last");

            Map idObj = (Map) user.get("id");
            String userId = idObj == null ? null : (String) idObj.get("value");

            // sanitize weird/empty ids like "NaNNA303undefined"
            if (userId != null) {
                userId = userId.trim();
                String lower = userId.toLowerCase();
                if (userId.isEmpty() || lower.contains("undefined")
                        || lower.contains("nan")) {
                    userId = null;
                }
            }

            Map dob = (Map) user.get("dob");
            Number ageNum = (Number) dob.get("age");
            int age = ageNum == null ? 0 : ageNum.intValue();

            userWithPets.add(new UserWithPetDto(
                    userId,
                    (String) user.get("gender"),
                    (String) user.get("nat"),
                    fullName,
                    (String) user.get("email"),
                    new UserWithPetDto.Dob((String) dob.get("date"), age),
                    (String) user.get("phone"),
                    dogImageUrls.get(i)));
        }
        return userWithPets;
    }

    private static int clampResults(int results) {
        if (results < 1)
            return 1;
        if (results > 50)
            return 50; // Dog API limit for /random/{n}
        return results;
    }

    private static String normalizeAndValidateNat(String nat) {
        if (nat == null || nat.isBlank())
            return null;

        String[] parts = nat.split(",");
        List<String> cleaned = new ArrayList<>();

        for (String part : parts) {
            String code = part.trim().toUpperCase();
            if (code.isEmpty())
                continue;

            if (!ALLOWED_NATS.contains(code)) {
                throw new IllegalArgumentException("Unsupported nat code: " + code);
            }
            cleaned.add(code);
        }

        return cleaned.isEmpty() ? null : String.join(",", cleaned);
    }
}
