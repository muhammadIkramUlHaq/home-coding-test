import { useCallback, useState } from "react";
import { getUsersWithPet } from "../../../api/usersWithPet/usersWithPet.api";
import type { UserWithPet } from "../../../api/usersWithPet/usersWithPet.types";
import { getErrorMessage } from "../../../lib";

export type UsersWithPetQuery = {
  results: number;
  nat: string; // "" means all
  seed?: string;
};

export function useUsersWithPet(
  initial: UsersWithPetQuery = { results: 10, nat: "" }
) {
  const [query, setQuery] = useState<UsersWithPetQuery>(initial);
  const [data, setData] = useState<UserWithPet[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchUsers = useCallback(async () => {
    setError("");
    setLoading(true);
    try {
      const res = await getUsersWithPet({
        results: query.results,
        nat: query.nat || undefined,
        seed: query.seed || undefined,
      });
      setData(res);
    } catch (e) {
      setData([]);
      setError(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }, [query]);

  return {
    query,
    setQuery,
    data,
    loading,
    error,
    fetchUsers,
  };
}
