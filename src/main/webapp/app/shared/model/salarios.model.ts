import dayjs from 'dayjs';

export interface ISalarios {
  id?: number;
  valor?: number | null;
  nome?: string | null;
  dataRecebimento?: string | null;
}

export const defaultValue: Readonly<ISalarios> = {};
