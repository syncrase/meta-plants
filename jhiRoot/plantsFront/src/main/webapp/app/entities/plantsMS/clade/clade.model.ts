import { IAPGIIIPlante } from 'app/entities/plantsMS/apgiii-plante/apgiii-plante.model';

export interface IClade {
  id?: number;
  nom?: string;
  apgiiis?: IAPGIIIPlante[] | null;
}

export class Clade implements IClade {
  constructor(public id?: number, public nom?: string, public apgiiis?: IAPGIIIPlante[] | null) {}
}

export function getCladeIdentifier(clade: IClade): number | undefined {
  return clade.id;
}
