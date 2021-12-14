import { IForme } from 'app/entities/classificationMS/forme/forme.model';
import { IVariete } from 'app/entities/classificationMS/variete/variete.model';

export interface ISousVariete {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  formes?: IForme[] | null;
  synonymes?: ISousVariete[] | null;
  variete?: IVariete | null;
  sousVariete?: ISousVariete | null;
}

export class SousVariete implements ISousVariete {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public formes?: IForme[] | null,
    public synonymes?: ISousVariete[] | null,
    public variete?: IVariete | null,
    public sousVariete?: ISousVariete | null
  ) {}
}

export function getSousVarieteIdentifier(sousVariete: ISousVariete): number | undefined {
  return sousVariete.id;
}
