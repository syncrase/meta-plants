import { IDivision } from 'app/entities/classificationMS/division/division.model';
import { IInfraRegne } from 'app/entities/classificationMS/infra-regne/infra-regne.model';

export interface ISuperDivision {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  divisions?: IDivision[] | null;
  synonymes?: ISuperDivision[] | null;
  infraRegne?: IInfraRegne | null;
  superDivision?: ISuperDivision | null;
}

export class SuperDivision implements ISuperDivision {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public divisions?: IDivision[] | null,
    public synonymes?: ISuperDivision[] | null,
    public infraRegne?: IInfraRegne | null,
    public superDivision?: ISuperDivision | null
  ) {}
}

export function getSuperDivisionIdentifier(superDivision: ISuperDivision): number | undefined {
  return superDivision.id;
}
