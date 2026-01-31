import type { UserWithPet } from "#/api/usersWithPet/usersWithPet.types";
import { UserCard } from "./UserCard";

export function UsersGrid({ users }: { users: UserWithPet[] }) {
  return (
    <div className="usersGrid">
      {users.map((user) => (
        <UserCard key={user.email} user={user} />
      ))}
    </div>
  );
}
