import { IPlante } from 'app/entities/microservice/plante/plante.model';

export interface IAllelopathie {
  id?: number;
  type?: string;
  description?: string | null;
  cible?: IPlante | null;
  origine?: IPlante | null;
  interaction?: IPlante | null;
}

export class Allelopathie implements IAllelopathie {
  constructor(
    public id?: number,
    public type?: string,
    public description?: string | null,
    public cible?: IPlante | null,
    public origine?: IPlante | null,
    public interaction?: IPlante | null
  ) {}
}

export function getAllelopathieIdentifier(allelopathie: IAllelopathie): number | undefined {
  return allelopathie.id;
}
