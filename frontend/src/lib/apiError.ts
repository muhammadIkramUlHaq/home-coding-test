import type { AxiosError } from "axios";

export type BackendError = {
  timestamp: string;
  status: number;
  error: string;
  message: string;
};

function isBackendError(data: unknown): data is BackendError {
  if (!data || typeof data !== "object") return false;
  const backendErrorData = data as Record<string, unknown>;
  return (
    typeof backendErrorData.timestamp === "string" &&
    typeof backendErrorData.status === "number" &&
    typeof backendErrorData.error === "string" &&
    typeof backendErrorData.message === "string"
  );
}

export function getErrorMessage(error: unknown): string {
  const axiosError = error as AxiosError;

  if (axiosError?.isAxiosError) {
    const status = axiosError.response?.status;
    const contentType =
      (axiosError.response?.headers?.["content-type"] as string | undefined) ??
      "";

    // Vite proxy / dev server failure (backend down)
    if (status === 500 && contentType.includes("text/plain")) {
      return "Backend not reachable (is Spring Boot running on http://localhost:8080?)";
    }

    const data = axiosError.response?.data;

    if (isBackendError(data)) {
      return `${data.status} ${data.error}: ${data.message}`;
    }

    if (axiosError.response)
      return `Request failed (${axiosError.response.status})`;
    if (axiosError.request) return "Backend not reachable";

    return axiosError.message;
  }

  if (error instanceof Error) return error.message;
  return "Unknown error";
}
