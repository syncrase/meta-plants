import { ISousForme } from 'app/entities/classificationMS/sous-forme/sous-forme.model';
import { ISousVariete } from 'app/entities/classificationMS/sous-variete/sous-variete.model';

export interface IForme {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousFormes?: ISousForme[] | null;
  synonymes?: IForme[] | null;
  sousVariete?: ISousVariete | null;
  forme?: IForme | null;
}

export class Forme implements IForme {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousFormes?: ISousForme[] | null,
    public synonymes?: IForme[] | null,
    public sousVariete?: ISousVariete | null,
    public forme?: IForme | null
  ) {}
}

export function getFormeIdentifier(forme: IForme): number | undefined {
  return forme.id;
}
