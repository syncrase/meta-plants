import { IInfraRegne } from 'app/entities/classificationMS/infra-regne/infra-regne.model';
import { ISousRegne } from 'app/entities/classificationMS/sous-regne/sous-regne.model';

export interface IRameau {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  infraRegnes?: IInfraRegne[] | null;
  synonymes?: IRameau[] | null;
  sousRegne?: ISousRegne | null;
  rameau?: IRameau | null;
}

export class Rameau implements IRameau {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public infraRegnes?: IInfraRegne[] | null,
    public synonymes?: IRameau[] | null,
    public sousRegne?: ISousRegne | null,
    public rameau?: IRameau | null
  ) {}
}

export function getRameauIdentifier(rameau: IRameau): number | undefined {
  return rameau.id;
}
