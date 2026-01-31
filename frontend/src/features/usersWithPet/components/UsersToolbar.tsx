import type { UsersWithPetQuery } from "#/features/usersWithPet/hooks/useUsersWithPet";

const COUNTRY_OPTIONS = [
  { label: "All Countries", value: "" },
  { label: "🇦🇺 Australia", value: "AU" },
  { label: "🇧🇷 Brazil", value: "BR" },
  { label: "🇨🇦 Canada", value: "CA" },
  { label: "🇨🇭 Switzerland", value: "CH" },
  { label: "🇩🇪 Germany", value: "DE" },
  { label: "🇩🇰 Denmark", value: "DK" },
  { label: "🇪🇸 Spain", value: "ES" },
  { label: "🇫🇮 Finland", value: "FI" },
  { label: "🇫🇷 France", value: "FR" },
  { label: "🇬🇧 United Kingdom", value: "GB" },
  { label: "🇮🇪 Ireland", value: "IE" },
  { label: "🇮🇳 India", value: "IN" },
  { label: "🇮🇷 Iran", value: "IR" },
  { label: "🇲🇽 Mexico", value: "MX" },
  { label: "🇳🇱 Netherlands", value: "NL" },
  { label: "🇳🇴 Norway", value: "NO" },
  { label: "🇳🇿 New Zealand", value: "NZ" },
  { label: "🇷🇸 Serbia", value: "RS" },
  { label: "🇹🇷 Turkey", value: "TR" },
  { label: "🇺🇦 Ukraine", value: "UA" },
  { label: "🇺🇸 United States", value: "US" },
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
          data-testid="country-select"
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
          data-testid="results-input"
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
          data-testid="fetch-button"
          onClick={onFetch}
          disabled={loading}
        >
          {loading ? "Fetching..." : "Fetch Data"}
        </button>

        {hasFetched && !loading ? (
          <div className="usersToolbarMeta" data-testid="results-meta">
            Showing <b>{resultsCount}</b> user{resultsCount === 1 ? "" : "s"}
          </div>
        ) : null}
      </div>
    </div>
  );
}
