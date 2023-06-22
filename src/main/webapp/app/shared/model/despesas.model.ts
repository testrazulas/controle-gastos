import dayjs from 'dayjs';

export interface IDespesas {
  id?: number;
  valor?: number | null;
  categoria?: string | null;
  data?: string | null;
}

export const defaultValue: Readonly<IDespesas> = {};
