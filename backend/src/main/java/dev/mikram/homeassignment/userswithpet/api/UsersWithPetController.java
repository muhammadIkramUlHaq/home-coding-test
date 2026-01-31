package dev.mikram.homeassignment.userswithpet.api;

import dev.mikram.homeassignment.userswithpet.dto.UserWithPetDto;
import dev.mikram.homeassignment.userswithpet.service.UsersWithPetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersWithPetController {

    private final UsersWithPetService service;

    public UsersWithPetController(UsersWithPetService service) {
        this.service = service;
    }

    @GetMapping("/api/users-with-pet")
    public List<UserWithPetDto> usersWithPet(
            @RequestParam(defaultValue = "10") int results,
            @RequestParam(required = false) String nat,
            @RequestParam(required = false) String seed) {

        return service.getUsersWithPet(results, nat, seed);
    }
}
