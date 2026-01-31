package dev.mikram.homeassignment.userswithpet.dto;

public record UserWithPetDto(
        String id,
        String gender,
        String country,
        String name,
        String email,
        Dob dob,
        String phone,
        String petImage) {
    public record Dob(String date, int age) {
    }
}
