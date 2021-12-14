import { ISuperOrdre } from 'app/entities/classificationMS/super-ordre/super-ordre.model';
import { ISousClasse } from 'app/entities/classificationMS/sous-classe/sous-classe.model';

export interface IInfraClasse {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  superOrdres?: ISuperOrdre[] | null;
  synonymes?: IInfraClasse[] | null;
  sousClasse?: ISousClasse | null;
  infraClasse?: IInfraClasse | null;
}

export class InfraClasse implements IInfraClasse {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public superOrdres?: ISuperOrdre[] | null,
    public synonymes?: IInfraClasse[] | null,
    public sousClasse?: ISousClasse | null,
    public infraClasse?: IInfraClasse | null
  ) {}
}

export function getInfraClasseIdentifier(infraClasse: IInfraClasse): number | undefined {
  return infraClasse.id;
}
