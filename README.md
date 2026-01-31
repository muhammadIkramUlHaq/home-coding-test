# Home Coding Test — Users With Pet 🐶

Small full-stack app that fetches users from **RandomUser** and assigns random dog images from **Dog API**, then displays them in a responsive UI.

- **Backend:** Spring Boot (Java)
- **Frontend:** React + Vite + TypeScript
- **E2E Tests:** Playwright
- **Backend Tests:** JUnit 5 + Mockito + MockMvc

---

## Repo Structure

```text
home-coding-test
├── backend/     # Spring Boot app
├── frontend/    # React/Vite app
└── package.json # root scripts to run E2E easily
```

---

# Prerequisites

Install before running:

- Java 17+
- Maven (`mvn`)
- Node.js 18+
- pnpm (`npm install -g pnpm`)

---

# Quick Start

##  Run full app (frontend + backend)

From the repo root:

```bash
pnpm install
pnpm dev
```
> **Why `pnpm dev` from repo root?**  
> This repo contains both frontend and backend. The root `pnpm dev` script starts **Spring Boot (8080)** and **Vite (5173)** together using `concurrently`, so anyone can run the full app with a single command.


## 1️⃣ Start Backend (manual alternative)

```bash
cd backend
mvn spring-boot:run
```

Backend runs on:

```bash
http://localhost:8080
```

Health check:
```bash
GET http://localhost:8080/api/health
```

Main API:

```bash
GET http://localhost:8080/api/users-with-pet
```


## 2️⃣ Start Frontend (manual alternative)
Open another terminal
```bash
cd frontend
pnpm install
pnpm run dev
```

Frontend runs on:

```bash
http://localhost:5173
```


# Running Tests

## Backend — Unit + MVC tests
```bash
cd backend
mvn test
```

Run specific test class:

```bash
mvn -Dtest=UsersWithPetServiceTest test
mvn -Dtest=UsersWithPetControllerMvcTest test
```

## Frontend — E2E tests (Playwright)

Run from repo root:

```bash
pnpm install
pnpm test:e2e
```

This will:

- start backend

- start frontend

- wait for readiness

- run Playwright tests

- shut down servers automatically

# Useful Commands

## Backend

```bash
cd backend
mvn spring-boot:run
mvn test
```

## Frontend

```bash
cd frontend
pnpm dev
```

## E2E (root)

```bash
pnpm test:e2e
```

