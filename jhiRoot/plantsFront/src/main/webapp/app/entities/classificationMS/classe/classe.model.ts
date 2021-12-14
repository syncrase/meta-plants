import { ISousClasse } from 'app/entities/classificationMS/sous-classe/sous-classe.model';
import { ISuperClasse } from 'app/entities/classificationMS/super-classe/super-classe.model';

export interface IClasse {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousClasses?: ISousClasse[] | null;
  synonymes?: IClasse[] | null;
  superClasse?: ISuperClasse | null;
  classe?: IClasse | null;
}

export class Classe implements IClasse {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousClasses?: ISousClasse[] | null,
    public synonymes?: IClasse[] | null,
    public superClasse?: ISuperClasse | null,
    public classe?: IClasse | null
  ) {}
}

export function getClasseIdentifier(classe: IClasse): number | undefined {
  return classe.id;
}
