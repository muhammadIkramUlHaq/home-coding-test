import type { UserWithPet } from "#/api/usersWithPet/usersWithPet.types";
import { UserCard } from "./UserCard";

export function UsersGrid({ users }: { users: UserWithPet[] }) {
  return (
    <div data-testid="users-grid" className="usersGrid">
      {users.map((user) => (
        <UserCard key={user.email} user={user} />
      ))}
    </div>
  );
}
