export type UserDob = {
  date: string;
  age: number;
};

export type UserWithPet = {
  id: string | null;
  gender: string;
  country: string;
  name: string;
  email: string;
  dob: UserDob;
  phone: string;
  petImage: string;
};

export type GetUsersWithPetParams = {
  results?: number; // default 10 on backend
  nat?: string;
  seed?: string;
};
