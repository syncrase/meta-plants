import { IPlante } from 'app/entities/microservice/plante/plante.model';

export interface ISol {
  id?: number;
  phMin?: number | null;
  phMax?: number | null;
  type?: string | null;
  richesse?: string | null;
  plante?: IPlante | null;
}

export class Sol implements ISol {
  constructor(
    public id?: number,
    public phMin?: number | null,
    public phMax?: number | null,
    public type?: string | null,
    public richesse?: string | null,
    public plante?: IPlante | null
  ) {}
}

export function getSolIdentifier(sol: ISol): number | undefined {
  return sol.id;
}
