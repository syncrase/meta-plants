import { IPlante } from 'app/entities/microservice/plante/plante.model';

export interface IStrate {
  id?: number;
  type?: string | null;
  plantes?: IPlante[] | null;
}

export class Strate implements IStrate {
  constructor(public id?: number, public type?: string | null, public plantes?: IPlante[] | null) {}
}

export function getStrateIdentifier(strate: IStrate): number | undefined {
  return strate.id;
}
