import type { AxiosError } from "axios";

export type BackendError = {
  timestamp: string;
  status: number;
  error: string;
  message: string;
};

export function getErrorMessage(err: unknown): string {
  // Axios error?
  const maybeAxios = err as AxiosError<BackendError>;

  if (maybeAxios?.isAxiosError) {
    const data = maybeAxios.response?.data;
    if (data?.message) return `${data.status} ${data.error}: ${data.message}`;
    return maybeAxios.message || "Request failed";
  }

  if (err instanceof Error) return err.message;
  return "Something went wrong";
}
