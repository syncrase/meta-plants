import { IRameau } from 'app/entities/classificationMS/rameau/rameau.model';
import { IRegne } from 'app/entities/classificationMS/regne/regne.model';

export interface ISousRegne {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  rameaus?: IRameau[] | null;
  synonymes?: ISousRegne[] | null;
  regne?: IRegne | null;
  sousRegne?: ISousRegne | null;
}

export class SousRegne implements ISousRegne {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public rameaus?: IRameau[] | null,
    public synonymes?: ISousRegne[] | null,
    public regne?: IRegne | null,
    public sousRegne?: ISousRegne | null
  ) {}
}

export function getSousRegneIdentifier(sousRegne: ISousRegne): number | undefined {
  return sousRegne.id;
}
