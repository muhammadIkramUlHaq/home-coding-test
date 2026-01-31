package dev.mikram.homeassignment.userswithpet.service;

import dev.mikram.homeassignment.userswithpet.client.DogApiClient;
import dev.mikram.homeassignment.userswithpet.client.RandomUserClient;
import dev.mikram.homeassignment.userswithpet.dto.UserWithPetDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersWithPetServiceTest {

    private RandomUserClient randomUserClient;
    private DogApiClient dogApiClient;
    private UsersWithPetService service;

    @BeforeEach
    void setUp() {
        randomUserClient = mock(RandomUserClient.class);
        dogApiClient = mock(DogApiClient.class);
        service = new UsersWithPetService(randomUserClient, dogApiClient);
    }

    @Test
    void clampsResults_belowOne_toOne_andPassesToClients() {
        // Arrange
        when(randomUserClient.fetchUsers(eq(1), any(), any())).thenReturn(randomUserResponseWithUsers(1));
        when(dogApiClient.fetchDogImages(eq(1))).thenReturn(dogApiResponseWithImages(1));

        // Act
        List<UserWithPetDto> res = service.getUsersWithPet(0, "FI", null);

        // Assert
        assertEquals(1, res.size());
        verify(randomUserClient).fetchUsers(eq(1), eq("FI"), isNull());
        verify(dogApiClient).fetchDogImages(eq(1));
    }

    @Test
    void clampsResults_aboveFifty_toFifty_andPassesToClients() {
        // Arrange
        when(randomUserClient.fetchUsers(eq(50), any(), any())).thenReturn(randomUserResponseWithUsers(2));
        when(dogApiClient.fetchDogImages(eq(50))).thenReturn(dogApiResponseWithImages(2));

        // Act
        List<UserWithPetDto> res = service.getUsersWithPet(999, "FI", "seed123");

        // Assert
        assertEquals(2, res.size());
        verify(randomUserClient).fetchUsers(eq(50), eq("FI"), eq("seed123"));
        verify(dogApiClient).fetchDogImages(eq(50));
    }

    @Test
    void nat_nullOrBlank_isTreatedAsNoFilter_andPassesNullToClient() {
        // Arrange
        when(randomUserClient.fetchUsers(eq(10), isNull(), any())).thenReturn(randomUserResponseWithUsers(1));
        when(dogApiClient.fetchDogImages(eq(10))).thenReturn(dogApiResponseWithImages(1));

        // Act
        service.getUsersWithPet(10, "   ", null);

        // Assert
        verify(randomUserClient).fetchUsers(eq(10), isNull(), isNull());
    }

    @Test
    void nat_isNormalized_uppercased_andTrimmed_single() {
        // Arrange
        when(randomUserClient.fetchUsers(eq(10), any(), any())).thenReturn(randomUserResponseWithUsers(1));
        when(dogApiClient.fetchDogImages(eq(10))).thenReturn(dogApiResponseWithImages(1));

        // Act
        service.getUsersWithPet(10, " fi ", null);

        // Assert
        verify(randomUserClient).fetchUsers(eq(10), eq("FI"), isNull());
    }

    @Test
    void nat_multipleCodes_isNormalized_andCommaJoined() {
        // Arrange
        when(randomUserClient.fetchUsers(eq(10), any(), any())).thenReturn(randomUserResponseWithUsers(1));
        when(dogApiClient.fetchDogImages(eq(10))).thenReturn(dogApiResponseWithImages(1));

        // Act
        service.getUsersWithPet(10, "fi, de ,  ", null);

        // Assert
        verify(randomUserClient).fetchUsers(eq(10), eq("FI,DE"), isNull());
    }

    @Test
    void nat_unsupported_throwsIllegalArgumentException() {
        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.getUsersWithPet(10, "SE", null));

        assertEquals("Unsupported nat code: SE", ex.getMessage());
        verifyNoInteractions(randomUserClient, dogApiClient);
    }

    @Test
    void returnsMinCount_whenUsersMoreThanDogs() {
        // Arrange: 3 users, 2 dogs -> should return 2
        when(randomUserClient.fetchUsers(eq(10), any(), any())).thenReturn(randomUserResponseWithUsers(3));
        when(dogApiClient.fetchDogImages(eq(10))).thenReturn(dogApiResponseWithImages(2));

        // Act
        List<UserWithPetDto> res = service.getUsersWithPet(10, "FI", null);

        // Assert
        assertEquals(2, res.size());
        assertEquals("https://dog/0.jpg", res.get(0).petImage());
        assertEquals("https://dog/1.jpg", res.get(1).petImage());
    }

    @Test
    void mapsFields_correctly_forFirstUser() {
        // Arrange
        Map<String, Object> randomUser = buildRandomUser(
                "female",
                "FI",
                "Siiri",
                "Lehto",
                "siiri.lehto@example.com",
                "1985-03-21T08:46:02.385Z",
                40,
                "03-867-490",
                Map.of("value", "ID-123"));

        when(randomUserClient.fetchUsers(eq(10), any(), any()))
                .thenReturn(Map.of("results", List.of(randomUser)));

        when(dogApiClient.fetchDogImages(eq(10)))
                .thenReturn(Map.of("message", List.of("https://dog/0.jpg")));

        // Act
        List<UserWithPetDto> res = service.getUsersWithPet(10, "FI", null);

        // Assert
        assertEquals(1, res.size());
        UserWithPetDto dto = res.get(0);

        assertEquals("ID-123", dto.id());
        assertEquals("female", dto.gender());
        assertEquals("FI", dto.country());
        assertEquals("Siiri Lehto", dto.name());
        assertEquals("siiri.lehto@example.com", dto.email());
        assertNotNull(dto.dob());
        assertEquals("1985-03-21T08:46:02.385Z", dto.dob().date());
        assertEquals(40, dto.dob().age());
        assertEquals("03-867-490", dto.phone());
        assertEquals("https://dog/0.jpg", dto.petImage());
    }

    @Test
    void sanitizesUserId_whenEmpty_undefined_orNan() {
        // Arrange: 3 users with problematic ids
        Map<String, Object> u1 = buildRandomUser("male", "FI", "A", "One", "a@x.com", "2000-01-01T00:00:00Z", 24, "1",
                Map.of("value", "   "));
        Map<String, Object> u2 = buildRandomUser("male", "FI", "B", "Two", "b@x.com", "2000-01-01T00:00:00Z", 24, "2",
                Map.of("value", "NaNNA303undefined"));
        Map<String, Object> u3 = buildRandomUser("male", "FI", "C", "Three", "c@x.com", "2000-01-01T00:00:00Z", 24, "3",
                Map.of("value", "NaN-123"));

        when(randomUserClient.fetchUsers(eq(10), any(), any()))
                .thenReturn(Map.of("results", List.of(u1, u2, u3)));

        when(dogApiClient.fetchDogImages(eq(10)))
                .thenReturn(Map.of("message", List.of("https://dog/0.jpg", "https://dog/1.jpg", "https://dog/2.jpg")));

        // Act
        List<UserWithPetDto> res = service.getUsersWithPet(10, "FI", null);

        // Assert
        assertEquals(3, res.size());
        assertNull(res.get(0).id());
        assertNull(res.get(1).id());
        assertNull(res.get(2).id());
    }

    @Test
    void passesSeed_throughToRandomUserClient() {
        // Arrange
        when(randomUserClient.fetchUsers(eq(10), any(), eq("seedX"))).thenReturn(randomUserResponseWithUsers(1));
        when(dogApiClient.fetchDogImages(eq(10))).thenReturn(dogApiResponseWithImages(1));

        // Act
        service.getUsersWithPet(10, "FI", "seedX");

        // Assert
        verify(randomUserClient).fetchUsers(eq(10), eq("FI"), eq("seedX"));
    }

    // --------------------
    // Helpers
    // --------------------

    private static Map<String, Object> randomUserResponseWithUsers(int n) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            results.add(buildRandomUser(
                    "male",
                    "FI",
                    "First" + i,
                    "Last" + i,
                    "u" + i + "@example.com",
                    "2000-01-01T00:00:00Z",
                    20 + i,
                    "000-" + i,
                    Map.of("value", "ID-" + i)));
        }
        return Map.of("results", results);
    }

    private static Map<String, Object> dogApiResponseWithImages(int n) {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            urls.add("https://dog/" + i + ".jpg");
        }
        return Map.of("message", urls);
    }

    private static Map<String, Object> buildRandomUser(
            String gender,
            String nat,
            String first,
            String last,
            String email,
            String dobDate,
            int age,
            String phone,
            Map<String, Object> idObj) {
        Map<String, Object> name = new HashMap<>();
        name.put("first", first);
        name.put("last", last);

        Map<String, Object> dob = new HashMap<>();
        dob.put("date", dobDate);
        dob.put("age", age);

        Map<String, Object> user = new HashMap<>();
        user.put("gender", gender);
        user.put("nat", nat);
        user.put("name", name);
        user.put("email", email);
        user.put("dob", dob);
        user.put("phone", phone);
        user.put("id", idObj);

        return user;
    }
}
