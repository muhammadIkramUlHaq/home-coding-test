import { useUsersWithPet } from "#/features/usersWithPet/hooks/useUsersWithPet";
import { UsersToolbar } from "#/features/usersWithPet/components/UsersToolbar";
import { UsersGrid } from "#/features/usersWithPet/components/UsersGrid";
import { UsersSkeletonGrid } from "#/features/usersWithPet/components/UsersSkeletonGrid";

export default function UsersWithPetPage() {
  const { query, setQuery, data, loading, error, hasFetched, fetchUsers } =
    useUsersWithPet({
      results: 10,
      nat: "",
    });

  return (
    <div className="page">
      <div className="panel">
        <h1 className="headerTitle">Users & Their Pets</h1>
        <p className="headerSubtitle">
          Browse users from different countries and meet their random dog
          companions.
        </p>

        <UsersToolbar
          query={query}
          onChange={setQuery}
          onFetch={fetchUsers}
          loading={loading}
          hasFetched={hasFetched}
          resultsCount={data.length}
        />

        {error ? (
          <div className="alert">
            <b>Error:</b> {error}
          </div>
        ) : null}

        {loading ? (
          <UsersSkeletonGrid count={Math.min(query.results, 9)} />
        ) : null}

        {!loading && !error && hasFetched && data.length === 0 ? (
          <div className="emptyState">
            No users returned. Try another filter.
          </div>
        ) : null}

        {!loading && data.length > 0 ? <UsersGrid users={data} /> : null}
      </div>
    </div>
  );
}
