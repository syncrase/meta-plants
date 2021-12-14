import { ISuperClasse } from 'app/entities/classificationMS/super-classe/super-classe.model';
import { IInfraEmbranchement } from 'app/entities/classificationMS/infra-embranchement/infra-embranchement.model';

export interface IMicroEmbranchement {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  superClasses?: ISuperClasse[] | null;
  synonymes?: IMicroEmbranchement[] | null;
  infraEmbranchement?: IInfraEmbranchement | null;
  microEmbranchement?: IMicroEmbranchement | null;
}

export class MicroEmbranchement implements IMicroEmbranchement {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public superClasses?: ISuperClasse[] | null,
    public synonymes?: IMicroEmbranchement[] | null,
    public infraEmbranchement?: IInfraEmbranchement | null,
    public microEmbranchement?: IMicroEmbranchement | null
  ) {}
}

export function getMicroEmbranchementIdentifier(microEmbranchement: IMicroEmbranchement): number | undefined {
  return microEmbranchement.id;
}
