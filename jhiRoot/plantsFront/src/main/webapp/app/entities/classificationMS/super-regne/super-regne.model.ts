import { IRegne } from 'app/entities/classificationMS/regne/regne.model';

export interface ISuperRegne {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  regnes?: IRegne[] | null;
  synonymes?: ISuperRegne[] | null;
  superRegne?: ISuperRegne | null;
}

export class SuperRegne implements ISuperRegne {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public regnes?: IRegne[] | null,
    public synonymes?: ISuperRegne[] | null,
    public superRegne?: ISuperRegne | null
  ) {}
}

export function getSuperRegneIdentifier(superRegne: ISuperRegne): number | undefined {
  return superRegne.id;
}
