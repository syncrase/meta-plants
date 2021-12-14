import { ISousDivision } from 'app/entities/classificationMS/sous-division/sous-division.model';
import { ISuperDivision } from 'app/entities/classificationMS/super-division/super-division.model';

export interface IDivision {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousDivisions?: ISousDivision[] | null;
  synonymes?: IDivision[] | null;
  superDivision?: ISuperDivision | null;
  division?: IDivision | null;
}

export class Division implements IDivision {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousDivisions?: ISousDivision[] | null,
    public synonymes?: IDivision[] | null,
    public superDivision?: ISuperDivision | null,
    public division?: IDivision | null
  ) {}
}

export function getDivisionIdentifier(division: IDivision): number | undefined {
  return division.id;
}
