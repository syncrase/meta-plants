import { IPlante } from 'app/entities/microservice/plante/plante.model';

export interface IRessemblance {
  id?: number;
  description?: string | null;
  confusion?: IPlante | null;
}

export class Ressemblance implements IRessemblance {
  constructor(public id?: number, public description?: string | null, public confusion?: IPlante | null) {}
}

export function getRessemblanceIdentifier(ressemblance: IRessemblance): number | undefined {
  return ressemblance.id;
}
