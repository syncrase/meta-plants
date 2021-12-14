import { IInfraClasse } from 'app/entities/classificationMS/infra-classe/infra-classe.model';
import { IClasse } from 'app/entities/classificationMS/classe/classe.model';

export interface ISousClasse {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  infraClasses?: IInfraClasse[] | null;
  synonymes?: ISousClasse[] | null;
  classe?: IClasse | null;
  sousClasse?: ISousClasse | null;
}

export class SousClasse implements ISousClasse {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public infraClasses?: IInfraClasse[] | null,
    public synonymes?: ISousClasse[] | null,
    public classe?: IClasse | null,
    public sousClasse?: ISousClasse | null
  ) {}
}

export function getSousClasseIdentifier(sousClasse: ISousClasse): number | undefined {
  return sousClasse.id;
}
