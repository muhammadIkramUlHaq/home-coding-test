import type { UserWithPet } from "#/api/usersWithPet/usersWithPet.types";

export function UserCard({ user }: { user: UserWithPet }) {
  const dob = user.dob?.date
    ? new Date(user.dob.date).toLocaleDateString()
    : "";

  return (
    <article data-testid="user-card" className="userCard">
      <div className="userCardImageWrap">
        <img
          className="userCardImage"
          src={user.petImage}
          alt={`Dog for ${user.name}`}
          loading="lazy"
          onError={(e) => {
            (e.currentTarget as HTMLImageElement).src =
              "https://via.placeholder.com/800x500?text=Dog+Image";
          }}
        />
      </div>

      <div className="userCardBody">
        <div className="userCardTopRow">
          <h3 className="userCardName">{user.name}</h3>
          <span data-testid="country-pill" className="userCardPill">
            {user.country}
          </span>
        </div>

        <div className="userCardInfoGrid">
          <Info label="Age" value={String(user.dob?.age ?? "")} />
          <Info label="Gender" value={user.gender} />
        </div>

        <Row label="Email">
          <a href={`mailto:${user.email}`}>{user.email}</a>
        </Row>

        <Row label="Phone">
          <a href={`tel:${user.phone}`}>{user.phone}</a>
        </Row>

        <Row label="DOB">{dob}</Row>
      </div>
    </article>
  );
}

function Info({ label, value }: { label: string; value: string }) {
  return (
    <div>
      <div className="k">{label}</div>
      <div className="v">{value}</div>
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
    <div className="userCardRow">
      <span className="k">{label}</span>
      <span className="v userCardRowValue">{children}</span>
    </div>
  );
}
