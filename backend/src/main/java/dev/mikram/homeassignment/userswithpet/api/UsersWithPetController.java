package dev.mikram.homeassignment.userswithpet.api;

import dev.mikram.homeassignment.userswithpet.dto.UserWithPetDto;
import dev.mikram.homeassignment.userswithpet.service.UsersWithPetService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users-with-pet", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersWithPetController {

    private final UsersWithPetService service;

    public UsersWithPetController(UsersWithPetService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserWithPetDto> usersWithPet(
            @RequestParam(defaultValue = "10") int results,
            @RequestParam(required = false) String nat,
            @RequestParam(required = false) String seed) {
        return service.getUsersWithPet(results, nat, seed);
    }
}
