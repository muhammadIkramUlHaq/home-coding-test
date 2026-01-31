import { useUsersWithPet } from "#/features/usersWithPet/hooks/useUsersWithPet";
import { UsersToolbar } from "#/features/usersWithPet/components/UsersToolbar";
import { UsersGrid } from "#/features/usersWithPet/components/UsersGrid";
import { UsersSkeletonGrid } from "#/features/usersWithPet/components/UsersSkeletonGrid";

export default function UsersWithPetPage() {
  const { query, setQuery, data, loading, error, fetchUsers } = useUsersWithPet(
    {
      results: 10,
      nat: "SE",
    }
  );

  return (
    <div className="page">
      <div className="panel">
        <h1 className="headerTitle">Users With Pet</h1>
        <p className="headerSubtitle">
          Fetch users from your backend and show them with random dog images.
        </p>

        <UsersToolbar
          query={query}
          onChange={setQuery}
          onFetch={fetchUsers}
          loading={loading}
        />

        {error ? (
          <div className="alert">
            <b>Error:</b> {error}
          </div>
        ) : null}

        {loading ? (
          <UsersSkeletonGrid count={Math.min(query.results, 9)} />
        ) : null}

        {!loading && !error && data.length === 0 ? (
          <div className="emptyState">
            No data yet. Choose filters and click <b>Fetch Data</b>.
          </div>
        ) : null}

        {!loading && data.length > 0 ? <UsersGrid users={data} /> : null}
      </div>
    </div>
  );
}
