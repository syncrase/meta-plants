import { IClasse } from 'app/entities/classificationMS/classe/classe.model';
import { IMicroEmbranchement } from 'app/entities/classificationMS/micro-embranchement/micro-embranchement.model';

export interface ISuperClasse {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  classes?: IClasse[] | null;
  synonymes?: ISuperClasse[] | null;
  microEmbranchement?: IMicroEmbranchement | null;
  superClasse?: ISuperClasse | null;
}

export class SuperClasse implements ISuperClasse {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public classes?: IClasse[] | null,
    public synonymes?: ISuperClasse[] | null,
    public microEmbranchement?: IMicroEmbranchement | null,
    public superClasse?: ISuperClasse | null
  ) {}
}

export function getSuperClasseIdentifier(superClasse: ISuperClasse): number | undefined {
  return superClasse.id;
}
