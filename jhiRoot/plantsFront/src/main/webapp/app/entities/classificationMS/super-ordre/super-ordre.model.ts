import { IOrdre } from 'app/entities/classificationMS/ordre/ordre.model';
import { IInfraClasse } from 'app/entities/classificationMS/infra-classe/infra-classe.model';

export interface ISuperOrdre {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  ordres?: IOrdre[] | null;
  synonymes?: ISuperOrdre[] | null;
  infraClasse?: IInfraClasse | null;
  superOrdre?: ISuperOrdre | null;
}

export class SuperOrdre implements ISuperOrdre {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public ordres?: IOrdre[] | null,
    public synonymes?: ISuperOrdre[] | null,
    public infraClasse?: IInfraClasse | null,
    public superOrdre?: ISuperOrdre | null
  ) {}
}

export function getSuperOrdreIdentifier(superOrdre: ISuperOrdre): number | undefined {
  return superOrdre.id;
}
