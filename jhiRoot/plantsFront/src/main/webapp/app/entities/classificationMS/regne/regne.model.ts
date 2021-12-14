import { ISousRegne } from 'app/entities/classificationMS/sous-regne/sous-regne.model';
import { ISuperRegne } from 'app/entities/classificationMS/super-regne/super-regne.model';

export interface IRegne {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousRegnes?: ISousRegne[] | null;
  synonymes?: IRegne[] | null;
  superRegne?: ISuperRegne | null;
  regne?: IRegne | null;
}

export class Regne implements IRegne {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousRegnes?: ISousRegne[] | null,
    public synonymes?: IRegne[] | null,
    public superRegne?: ISuperRegne | null,
    public regne?: IRegne | null
  ) {}
}

export function getRegneIdentifier(regne: IRegne): number | undefined {
  return regne.id;
}
