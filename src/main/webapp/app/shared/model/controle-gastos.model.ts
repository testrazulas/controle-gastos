import dayjs from 'dayjs';

export interface IControleGastos {
  id?: number;
  mes?: string | null;
}

export const defaultValue: Readonly<IControleGastos> = {};
