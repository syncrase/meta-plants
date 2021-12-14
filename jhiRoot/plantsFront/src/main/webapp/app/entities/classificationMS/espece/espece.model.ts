import { ISousEspece } from 'app/entities/classificationMS/sous-espece/sous-espece.model';
import { ISousSection } from 'app/entities/classificationMS/sous-section/sous-section.model';

export interface IEspece {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousEspeces?: ISousEspece[] | null;
  synonymes?: IEspece[] | null;
  sousSection?: ISousSection | null;
  espece?: IEspece | null;
}

export class Espece implements IEspece {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousEspeces?: ISousEspece[] | null,
    public synonymes?: IEspece[] | null,
    public sousSection?: ISousSection | null,
    public espece?: IEspece | null
  ) {}
}

export function getEspeceIdentifier(espece: IEspece): number | undefined {
  return espece.id;
}
