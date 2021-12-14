import { ITribu } from 'app/entities/classificationMS/tribu/tribu.model';
import { IFamille } from 'app/entities/classificationMS/famille/famille.model';

export interface ISousFamille {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  tribuses?: ITribu[] | null;
  synonymes?: ISousFamille[] | null;
  famille?: IFamille | null;
  sousFamille?: ISousFamille | null;
}

export class SousFamille implements ISousFamille {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public tribuses?: ITribu[] | null,
    public synonymes?: ISousFamille[] | null,
    public famille?: IFamille | null,
    public sousFamille?: ISousFamille | null
  ) {}
}

export function getSousFamilleIdentifier(sousFamille: ISousFamille): number | undefined {
  return sousFamille.id;
}
