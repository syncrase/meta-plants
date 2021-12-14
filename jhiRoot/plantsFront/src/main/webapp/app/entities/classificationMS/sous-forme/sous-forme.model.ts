import { IForme } from 'app/entities/classificationMS/forme/forme.model';

export interface ISousForme {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  synonymes?: ISousForme[] | null;
  forme?: IForme | null;
  sousForme?: ISousForme | null;
}

export class SousForme implements ISousForme {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public synonymes?: ISousForme[] | null,
    public forme?: IForme | null,
    public sousForme?: ISousForme | null
  ) {}
}

export function getSousFormeIdentifier(sousForme: ISousForme): number | undefined {
  return sousForme.id;
}
