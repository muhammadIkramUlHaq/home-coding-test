package dev.mikram.homeassignment.userswithpet.api;

import dev.mikram.homeassignment.common.error.ApiExceptionHandler;
import dev.mikram.homeassignment.common.error.DownstreamApiException;
import dev.mikram.homeassignment.userswithpet.dto.UserWithPetDto;
import dev.mikram.homeassignment.userswithpet.service.UsersWithPetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsersWithPetController.class)
@Import(ApiExceptionHandler.class)
class UsersWithPetControllerMvcTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UsersWithPetService service;

        @Test
        void getUsersWithPet_returns200_andJsonArray() throws Exception {
                // Arrange
                List<UserWithPetDto> payload = List.of(
                                new UserWithPetDto(
                                                "ID-1",
                                                "female",
                                                "FI",
                                                "Siiri Lehto",
                                                "siiri.lehto@example.com",
                                                new UserWithPetDto.Dob("1985-03-21T08:46:02.385Z", 40),
                                                "03-867-490",
                                                "https://dog/0.jpg"),
                                new UserWithPetDto(
                                                "ID-2",
                                                "male",
                                                "FI",
                                                "Aapo Jutila",
                                                "aapo.jutila@example.com",
                                                new UserWithPetDto.Dob("1993-03-16T08:03:11.574Z", 32),
                                                "02-026-880",
                                                "https://dog/1.jpg"));

                when(service.getUsersWithPet(eq(10), eq("FI"), isNull())).thenReturn(payload);

                // Act + Assert
                mockMvc.perform(get("/api/users-with-pet")
                                .param("results", "10")
                                .param("nat", "FI"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].name").value("Siiri Lehto"))
                                .andExpect(jsonPath("$[0].country").value("FI"))
                                .andExpect(jsonPath("$[0].petImage").value("https://dog/0.jpg"))
                                .andExpect(jsonPath("$[1].name").value("Aapo Jutila"));
        }

        @Test
        void getUsersWithPet_defaultResults_is10_whenNotProvided() throws Exception {
                // Arrange: controller defaultValue = "10"
                when(service.getUsersWithPet(eq(10), isNull(), isNull()))
                                .thenReturn(List.of());

                // Act + Assert
                mockMvc.perform(get("/api/users-with-pet"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        void getUsersWithPet_passesNatAndSeedThrough() throws Exception {
                when(service.getUsersWithPet(eq(7), eq("FI"), eq("seed123")))
                                .thenReturn(List.of());

                mockMvc.perform(get("/api/users-with-pet")
                                .param("results", "7")
                                .param("nat", "FI")
                                .param("seed", "seed123"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        void getUsersWithPet_whenServiceThrowsIllegalArgumentException_returns400_withErrorBody() throws Exception {
                // Arrange
                when(service.getUsersWithPet(anyInt(), nullable(String.class), nullable(String.class)))
                                .thenThrow(new IllegalArgumentException("Unsupported nat code: SE"));

                // Act + Assert
                mockMvc.perform(get("/api/users-with-pet")
                                .param("results", "10")
                                .param("nat", "SE"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.error").value("Bad Request"))
                                .andExpect(jsonPath("$.message").value("Unsupported nat code: SE"))
                                .andExpect(jsonPath("$.timestamp").exists());
        }

        @Test
        void getUsersWithPet_whenServiceThrowsDownstreamException_returns502_withErrorBody() throws Exception {
                // Arrange
                when(service.getUsersWithPet(anyInt(), nullable(String.class), nullable(String.class)))
                                .thenThrow(new DownstreamApiException("Downstream failed"));

                // Act + Assert
                mockMvc.perform(get("/api/users-with-pet").param("results", "10"))
                                .andExpect(status().isBadGateway())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.status").value(502))
                                .andExpect(jsonPath("$.error").value("Bad Gateway"))
                                .andExpect(jsonPath("$.message").value("Downstream failed"))
                                .andExpect(jsonPath("$.timestamp").exists());
        }
}
