export function UsersSkeletonGrid({ count }: { count: number }) {
  return (
    <div className="usersGrid" style={{ marginTop: 16 }}>
      {Array.from({ length: count }).map((_, i) => (
        <div key={i} className="userCard skeleton">
          <div className="userCardImageWrap skelBox" />
          <div className="userCardBody">
            <div className="skelLine" style={{ width: "60%", height: 12 }} />
            <div className="skelLine" style={{ width: "35%", marginTop: 6 }} />
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "1fr 1fr",
                gap: 10,
                marginTop: 10,
              }}
            >
              <div className="skelLine" style={{ height: 34 }} />
              <div className="skelLine" style={{ height: 34 }} />
            </div>
            <div className="skelLine" style={{ width: "80%", marginTop: 10 }} />
            <div className="skelLine" style={{ width: "70%", marginTop: 8 }} />
            <div className="skelLine" style={{ width: "50%", marginTop: 8 }} />
          </div>
        </div>
      ))}
    </div>
  );
}
