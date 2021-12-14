import { ISousTribu } from 'app/entities/classificationMS/sous-tribu/sous-tribu.model';
import { ISousFamille } from 'app/entities/classificationMS/sous-famille/sous-famille.model';

export interface ITribu {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousTribuses?: ISousTribu[] | null;
  synonymes?: ITribu[] | null;
  sousFamille?: ISousFamille | null;
  tribu?: ITribu | null;
}

export class Tribu implements ITribu {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousTribuses?: ISousTribu[] | null,
    public synonymes?: ITribu[] | null,
    public sousFamille?: ISousFamille | null,
    public tribu?: ITribu | null
  ) {}
}

export function getTribuIdentifier(tribu: ITribu): number | undefined {
  return tribu.id;
}
