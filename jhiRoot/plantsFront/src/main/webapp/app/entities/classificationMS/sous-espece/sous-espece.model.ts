import { IVariete } from 'app/entities/classificationMS/variete/variete.model';
import { IEspece } from 'app/entities/classificationMS/espece/espece.model';

export interface ISousEspece {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  varietes?: IVariete[] | null;
  synonymes?: ISousEspece[] | null;
  espece?: IEspece | null;
  sousEspece?: ISousEspece | null;
}

export class SousEspece implements ISousEspece {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public varietes?: IVariete[] | null,
    public synonymes?: ISousEspece[] | null,
    public espece?: IEspece | null,
    public sousEspece?: ISousEspece | null
  ) {}
}

export function getSousEspeceIdentifier(sousEspece: ISousEspece): number | undefined {
  return sousEspece.id;
}
