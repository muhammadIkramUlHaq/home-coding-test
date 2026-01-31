import { apiClient } from "#/lib";
import type { GetUsersWithPetParams, UserWithPet } from "./usersWithPet.types";

export async function getUsersWithPet(params: GetUsersWithPetParams) {
  const res = await apiClient.get<UserWithPet[]>("/api/users-with-pet", {
    params,
  });
  return res.data;
}
