import { ISousVariete } from 'app/entities/classificationMS/sous-variete/sous-variete.model';
import { ISousEspece } from 'app/entities/classificationMS/sous-espece/sous-espece.model';

export interface IVariete {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousVarietes?: ISousVariete[] | null;
  synonymes?: IVariete[] | null;
  sousEspece?: ISousEspece | null;
  variete?: IVariete | null;
}

export class Variete implements IVariete {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousVarietes?: ISousVariete[] | null,
    public synonymes?: IVariete[] | null,
    public sousEspece?: ISousEspece | null,
    public variete?: IVariete | null
  ) {}
}

export function getVarieteIdentifier(variete: IVariete): number | undefined {
  return variete.id;
}
