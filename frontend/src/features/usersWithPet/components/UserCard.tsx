import type { UserWithPet } from "../../../api/usersWithPet/usersWithPet.types";

export function UserCard({ user }: { user: UserWithPet }) {
  const dob = user.dob?.date
    ? new Date(user.dob.date).toLocaleDateString()
    : "";

  return (
    <article
      style={{
        border: "1px solid rgba(0,0,0,0.08)",
        borderRadius: 14,
        overflow: "hidden",
        background: "#fff",
        boxShadow: "0 8px 18px rgba(0,0,0,0.06)",
      }}
    >
      <div style={{ aspectRatio: "16 / 10", background: "#f3f4f6" }}>
        <img
          src={user.petImage}
          alt={`Dog for ${user.name}`}
          loading="lazy"
          style={{
            width: "100%",
            height: "100%",
            objectFit: "cover",
            display: "block",
          }}
          onError={(e) => {
            (e.currentTarget as HTMLImageElement).src =
              "https://via.placeholder.com/800x500?text=Dog+Image";
          }}
        />
      </div>

      <div style={{ padding: 12, display: "grid", gap: 10 }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            gap: 12,
            alignItems: "baseline",
          }}
        >
          <h3 style={{ margin: 0, fontSize: 16 }}>{user.name}</h3>
          <span
            style={{
              fontSize: 12,
              padding: "4px 10px",
              borderRadius: 999,
              border: "1px solid rgba(0,0,0,0.1)",
              color: "rgba(0,0,0,0.7)",
              whiteSpace: "nowrap",
            }}
          >
            {user.country}
          </span>
        </div>

        <div
          style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}
        >
          <Info label="Age" value={String(user.dob?.age ?? "")} />
          <Info label="Gender" value={user.gender} />
        </div>

        <div style={{ display: "grid", gap: 8 }}>
          <Row label="Email">
            <a
              href={`mailto:${user.email}`}
              style={{ color: "#2563eb", textDecoration: "none" }}
            >
              {user.email}
            </a>
          </Row>
          <Row label="Phone">
            <a
              href={`tel:${user.phone}`}
              style={{ color: "#2563eb", textDecoration: "none" }}
            >
              {user.phone}
            </a>
          </Row>
          <Row label="DOB">{dob}</Row>
        </div>
      </div>
    </article>
  );
}

function Info({ label, value }: { label: string; value: string }) {
  return (
    <div style={{ display: "grid", gap: 2 }}>
      <span style={{ fontSize: 12, color: "rgba(0,0,0,0.6)" }}>{label}</span>
      <span style={{ fontSize: 14, color: "rgba(0,0,0,0.9)" }}>{value}</span>
    </div>
  );
}

function Row({
  label,
  children,
}: {
  label: string;
  children: React.ReactNode;
}) {
  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: "64px 1fr",
        gap: 10,
        alignItems: "baseline",
      }}
    >
      <span style={{ fontSize: 12, color: "rgba(0,0,0,0.6)" }}>{label}</span>
      <span
        style={{
          fontSize: 14,
          color: "rgba(0,0,0,0.9)",
          overflowWrap: "anywhere",
        }}
      >
        {children}
      </span>
    </div>
  );
}
