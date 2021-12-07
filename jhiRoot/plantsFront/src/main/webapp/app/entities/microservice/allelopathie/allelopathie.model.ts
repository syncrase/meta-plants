import { IPlante } from 'app/entities/microservice/plante/plante.model';

export interface IAllelopathie {
  id?: number;
  type?: string;
  description?: string | null;
  impact?: number | null;
  cible?: IPlante | null;
  origine?: IPlante | null;
}

export class Allelopathie implements IAllelopathie {
  constructor(
    public id?: number,
    public type?: string,
    public description?: string | null,
    public impact?: number | null,
    public cible?: IPlante | null,
    public origine?: IPlante | null
  ) {}
}

export function getAllelopathieIdentifier(allelopathie: IAllelopathie): number | undefined {
  return allelopathie.id;
}
