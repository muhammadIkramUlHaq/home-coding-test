import { useState } from "react";
import { getUsersWithPet } from "../api/usersWithPet/usersWithPet.api";
import type { UserWithPet } from "../api/usersWithPet/usersWithPet.types";
import { getErrorMessage } from "../lib";

export default function UsersWithPetPage() {
  const [data, setData] = useState<UserWithPet[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function fetchData() {
    setError("");
    setLoading(true);

    try {
      const res = await getUsersWithPet({ results: 5, nat: "FI" });
      setData(res);
    } catch (e) {
      setData([]);
      setError(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: 16, fontFamily: "system-ui" }}>
      <h2>Users With Pet</h2>

      <button onClick={fetchData} disabled={loading}>
        {loading ? "Loading..." : "Fetch from backend"}
      </button>

      {error ? (
        <p style={{ color: "crimson", marginTop: 12 }}>
          <b>Error:</b> {error}
        </p>
      ) : null}

      <pre
        style={{
          marginTop: 12,
          background: "#111",
          color: "#eee",
          padding: 12,
          borderRadius: 8,
        }}
      >
        {JSON.stringify(data, null, 2)}
      </pre>
    </div>
  );
}
