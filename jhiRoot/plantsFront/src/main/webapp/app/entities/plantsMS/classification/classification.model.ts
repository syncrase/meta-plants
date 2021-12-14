import { IRaunkierPlante } from 'app/entities/plantsMS/raunkier-plante/raunkier-plante.model';
import { ICronquistPlante } from 'app/entities/plantsMS/cronquist-plante/cronquist-plante.model';
import { IAPGIPlante } from 'app/entities/plantsMS/apgi-plante/apgi-plante.model';
import { IAPGIIPlante } from 'app/entities/plantsMS/apgii-plante/apgii-plante.model';
import { IAPGIIIPlante } from 'app/entities/plantsMS/apgiii-plante/apgiii-plante.model';
import { IAPGIVPlante } from 'app/entities/plantsMS/apgiv-plante/apgiv-plante.model';
import { IPlante } from 'app/entities/plantsMS/plante/plante.model';

export interface IClassification {
  id?: number;
  raunkier?: IRaunkierPlante | null;
  cronquist?: ICronquistPlante | null;
  apg1?: IAPGIPlante | null;
  apg2?: IAPGIIPlante | null;
  apg3?: IAPGIIIPlante | null;
  apg4?: IAPGIVPlante | null;
  plante?: IPlante | null;
}

export class Classification implements IClassification {
  constructor(
    public id?: number,
    public raunkier?: IRaunkierPlante | null,
    public cronquist?: ICronquistPlante | null,
    public apg1?: IAPGIPlante | null,
    public apg2?: IAPGIIPlante | null,
    public apg3?: IAPGIIIPlante | null,
    public apg4?: IAPGIVPlante | null,
    public plante?: IPlante | null
  ) {}
}

export function getClassificationIdentifier(classification: IClassification): number | undefined {
  return classification.id;
}
