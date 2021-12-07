import { IPlante } from 'app/entities/microservice/plante/plante.model';

export interface IFeuillage {
  id?: number;
  type?: string | null;
  plantes?: IPlante[] | null;
}

export class Feuillage implements IFeuillage {
  constructor(public id?: number, public type?: string | null, public plantes?: IPlante[] | null) {}
}

export function getFeuillageIdentifier(feuillage: IFeuillage): number | undefined {
  return feuillage.id;
}
