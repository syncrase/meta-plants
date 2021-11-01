import { IPlante } from 'app/shared/model/microservice/plante.model';

export interface INomVernaculaire {
  id?: number;
  nom?: string;
  description?: string;
  plantes?: IPlante[];
}

export class NomVernaculaire implements INomVernaculaire {
  constructor(public id?: number, public nom?: string, public description?: string, public plantes?: IPlante[]) {}
}
