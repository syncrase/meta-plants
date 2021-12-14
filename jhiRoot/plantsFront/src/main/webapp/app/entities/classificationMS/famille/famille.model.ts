import { ISousFamille } from 'app/entities/classificationMS/sous-famille/sous-famille.model';
import { ISuperFamille } from 'app/entities/classificationMS/super-famille/super-famille.model';

export interface IFamille {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousFamilles?: ISousFamille[] | null;
  synonymes?: IFamille[] | null;
  superFamille?: ISuperFamille | null;
  famille?: IFamille | null;
}

export class Famille implements IFamille {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousFamilles?: ISousFamille[] | null,
    public synonymes?: IFamille[] | null,
    public superFamille?: ISuperFamille | null,
    public famille?: IFamille | null
  ) {}
}

export function getFamilleIdentifier(famille: IFamille): number | undefined {
  return famille.id;
}
