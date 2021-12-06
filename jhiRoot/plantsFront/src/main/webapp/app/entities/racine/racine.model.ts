import { IPlante } from 'app/entities/plante/plante.model';

export interface IRacine {
  id?: number;
  type?: string | null;
  plantes?: IPlante[] | null;
}

export class Racine implements IRacine {
  constructor(public id?: number, public type?: string | null, public plantes?: IPlante[] | null) {}
}

export function getRacineIdentifier(racine: IRacine): number | undefined {
  return racine.id;
}
