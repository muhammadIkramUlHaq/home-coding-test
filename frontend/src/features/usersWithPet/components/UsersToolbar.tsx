import type { UsersWithPetQuery } from "#/features/usersWithPet/hooks/useUsersWithPet";

const COUNTRY_OPTIONS = [
  { label: "All Countries", value: "" },
  { label: "AU", value: "AU" },
  { label: "BR", value: "BR" },
  { label: "CA", value: "CA" },
  { label: "CH", value: "CH" },
  { label: "DE", value: "DE" },
  { label: "DK", value: "DK" },
  { label: "ES", value: "ES" },
  { label: "FI", value: "FI" },
  { label: "FR", value: "FR" },
  { label: "GB", value: "GB" },
  { label: "IE", value: "IE" },
  { label: "IN", value: "IN" },
  { label: "IR", value: "IR" },
  { label: "MX", value: "MX" },
  { label: "NL", value: "NL" },
  { label: "NO", value: "NO" },
  { label: "NZ", value: "NZ" },
  { label: "RS", value: "RS" },
  { label: "TR", value: "TR" },
  { label: "UA", value: "UA" },
  { label: "US", value: "US" },
];

function clampInt(n: number, min: number, max: number) {
  if (Number.isNaN(n)) return min;
  return Math.max(min, Math.min(max, n));
}

export function UsersToolbar(props: {
  query: UsersWithPetQuery;
  onChange: (next: UsersWithPetQuery) => void;
  onFetch: () => void;
  loading: boolean;
  hasFetched: boolean;
  resultsCount: number;
}) {
  const { query, onChange, onFetch, loading, hasFetched, resultsCount } = props;

  return (
    <div className="usersToolbar">
      <label className="usersToolbarField">
        <span className="usersToolbarLabel">Filter by country</span>
        <select
          className="usersToolbarInput"
          value={query.nat}
          disabled={loading}
          onChange={(event) => onChange({ ...query, nat: event.target.value })}
        >
          {COUNTRY_OPTIONS.map((country) => (
            <option key={country.label} value={country.value}>
              {country.label}
            </option>
          ))}
        </select>
      </label>

      <label className="usersToolbarField">
        <span className="usersToolbarLabel">Number of users</span>
        <input
          className="usersToolbarInput"
          type="number"
          min={1}
          max={200}
          value={query.results}
          disabled={loading}
          onChange={(event) =>
            onChange({
              ...query,
              results: clampInt(Number(event.target.value), 1, 200),
            })
          }
        />
      </label>

      <div className="usersToolbarActions">
        <button
          className="usersToolbarButton"
          onClick={onFetch}
          disabled={loading}
        >
          {loading ? "Fetching..." : "Fetch Data"}
        </button>

        {hasFetched && !loading ? (
          <div className="usersToolbarMeta">
            Showing <b>{resultsCount}</b> user{resultsCount === 1 ? "" : "s"}
          </div>
        ) : null}
      </div>
    </div>
  );
}
