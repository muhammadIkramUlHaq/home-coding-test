export function PetIcon(props: React.SVGProps<SVGSVGElement>) {
  return (
    <svg
      viewBox="0 0 24 24"
      width="28"
      height="28"
      aria-hidden="true"
      {...props}
    >
      {/* circle ring */}
      <circle
        cx="12"
        cy="12"
        r="10"
        fill="none"
        stroke="currentColor"
        strokeWidth="2"
      />

      {/* left ear */}
      <path
        d="M7.7 10.2c-1.6-.2-2.4-1.6-2.5-3.4 1.8.1 3.5.9 3.7 2.6"
        fill="none"
        stroke="currentColor"
        strokeWidth="1.8"
        strokeLinecap="round"
        strokeLinejoin="round"
      />

      {/* right ear */}
      <path
        d="M16.3 10.2c1.6-.2 2.4-1.6 2.5-3.4-1.8.1-3.5.9-3.7 2.6"
        fill="none"
        stroke="currentColor"
        strokeWidth="1.8"
        strokeLinecap="round"
        strokeLinejoin="round"
      />

      {/* face/head */}
      <path
        d="M8.2 10.2v2.3c0 2.1 1.7 3.8 3.8 3.8h0c2.1 0 3.8-1.7 3.8-3.8v-2.3"
        fill="none"
        stroke="currentColor"
        strokeWidth="1.8"
        strokeLinecap="round"
        strokeLinejoin="round"
      />

      {/* eyes */}
      <path
        d="M10.4 12.6h.01M13.6 12.6h.01"
        fill="none"
        stroke="currentColor"
        strokeWidth="2.4"
        strokeLinecap="round"
      />

      {/* nose/muzzle */}
      <path
        d="M12 15c1.1 0 1.8-.8 1.8-1.7h-3.6c0 .9.7 1.7 1.8 1.7z"
        fill="none"
        stroke="currentColor"
        strokeWidth="1.8"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}
