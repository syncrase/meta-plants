import { IRaunkier } from 'app/entities/raunkier/raunkier.model';
import { ICronquist } from 'app/entities/cronquist/cronquist.model';
import { IAPGI } from 'app/entities/apgi/apgi.model';
import { IAPGII } from 'app/entities/apgii/apgii.model';
import { IAPGIII } from 'app/entities/apgiii/apgiii.model';
import { IAPGIV } from 'app/entities/apgiv/apgiv.model';

export interface IClassification {
  id?: number;
  nomLatin?: string | null;
  raunkier?: IRaunkier | null;
  cronquist?: ICronquist | null;
  apg1?: IAPGI | null;
  apg2?: IAPGII | null;
  apg3?: IAPGIII | null;
  apg4?: IAPGIV | null;
}

export class Classification implements IClassification {
  constructor(
    public id?: number,
    public nomLatin?: string | null,
    public raunkier?: IRaunkier | null,
    public cronquist?: ICronquist | null,
    public apg1?: IAPGI | null,
    public apg2?: IAPGII | null,
    public apg3?: IAPGIII | null,
    public apg4?: IAPGIV | null
  ) {}
}

export function getClassificationIdentifier(classification: IClassification): number | undefined {
  return classification.id;
}
