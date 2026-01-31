import { test, expect } from "@playwright/test";

test("fetches users and renders cards + count", async ({ page }) => {
  await page.goto("/");

  await page.getByTestId("results-input").fill("6");
  await page.getByTestId("fetch-button").click();

  await expect(page.getByTestId("results-meta")).toContainText("Showing 6");
  await expect(page.getByTestId("user-card")).toHaveCount(6);
});

test("filters by country selection (nat)", async ({ page }) => {
  await page.goto("/");

  await page.getByTestId("country-select").selectOption("FI");
  await page.getByTestId("results-input").fill("5");
  await page.getByTestId("fetch-button").click();

  await expect(page.getByTestId("results-meta")).toContainText("Showing 5");
  await expect(page.getByTestId("user-card")).toHaveCount(5);

  const pills = page.getByTestId("country-pill");
  await expect(pills).toHaveCount(5);

  const count = await pills.count();
  for (let i = 0; i < count; i++) {
    await expect(pills.nth(i)).toHaveText("FI");
  }
});
test("shows friendly error when request fails (mocked)", async ({ page }) => {
  await page.route("**/api/users-with-pet**", (route) => route.abort());

  await page.goto("/");
  await page.getByTestId("fetch-button").click();

  await expect(page.getByTestId("error-alert")).toContainText(
    /Backend not reachable/i
  );
});
