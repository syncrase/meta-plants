import { IPlante } from 'app/entities/microservice/plante/plante.model';

export interface IExposition {
  id?: number;
  valeur?: string | null;
  ensoleilement?: number | null;
  plante?: IPlante | null;
}

export class Exposition implements IExposition {
  constructor(public id?: number, public valeur?: string | null, public ensoleilement?: number | null, public plante?: IPlante | null) {}
}

export function getExpositionIdentifier(exposition: IExposition): number | undefined {
  return exposition.id;
}
