export interface ICartoes {
  id?: number;
  nome?: string | null;
  valor?: number | null;
}

export const defaultValue: Readonly<ICartoes> = {};
