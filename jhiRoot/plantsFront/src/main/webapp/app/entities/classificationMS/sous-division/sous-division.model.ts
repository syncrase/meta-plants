import { IInfraEmbranchement } from 'app/entities/classificationMS/infra-embranchement/infra-embranchement.model';
import { IDivision } from 'app/entities/classificationMS/division/division.model';

export interface ISousDivision {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  infraEmbranchements?: IInfraEmbranchement[] | null;
  synonymes?: ISousDivision[] | null;
  division?: IDivision | null;
  sousDivision?: ISousDivision | null;
}

export class SousDivision implements ISousDivision {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public infraEmbranchements?: IInfraEmbranchement[] | null,
    public synonymes?: ISousDivision[] | null,
    public division?: IDivision | null,
    public sousDivision?: ISousDivision | null
  ) {}
}

export function getSousDivisionIdentifier(sousDivision: ISousDivision): number | undefined {
  return sousDivision.id;
}
