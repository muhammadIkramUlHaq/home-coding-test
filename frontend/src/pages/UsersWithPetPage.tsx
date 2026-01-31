import { useUsersWithPet } from "../features/usersWithPet/hooks/useUsersWithPet";
import { UsersToolbar } from "../features/usersWithPet/components/UsersToolbar";
import { UsersGrid } from "../features/usersWithPet/components/UsersGrid";

export default function UsersWithPetPage() {
  const { query, setQuery, data, loading, error, fetchUsers } = useUsersWithPet(
    {
      results: 10,
      nat: "",
    }
  );

  return (
    <div
      style={{
        padding: 16,
        fontFamily: "system-ui",
        maxWidth: 1100,
        margin: "0 auto",
      }}
    >
      <h2 style={{ margin: "8px 0 4px" }}>Users With Pet</h2>
      <p style={{ margin: "0 0 16px", opacity: 0.75 }}>
        Fetch users from your backend and show them with random dog images.
      </p>

      <UsersToolbar
        query={query}
        onChange={setQuery}
        onFetch={fetchUsers}
        loading={loading}
      />

      {error ? (
        <div
          style={{
            marginTop: 14,
            padding: 12,
            borderRadius: 10,
            background: "#ffe6e6",
            color: "#7a0000",
          }}
        >
          <b>Error:</b> {error}
        </div>
      ) : null}

      {loading ? (
        <p style={{ marginTop: 16, opacity: 0.7 }}>Loading users...</p>
      ) : null}

      {!loading && !error && data.length === 0 ? (
        <div
          style={{
            marginTop: 16,
            padding: 14,
            border: "1px dashed rgba(0,0,0,0.2)",
            borderRadius: 12,
          }}
        >
          No data yet. Choose filters and click <b>Fetch Data</b>.
        </div>
      ) : null}

      {!loading && data.length > 0 ? <UsersGrid users={data} /> : null}
    </div>
  );
}
