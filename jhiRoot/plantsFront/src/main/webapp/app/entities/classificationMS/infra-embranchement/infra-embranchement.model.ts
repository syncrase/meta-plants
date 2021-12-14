import { IMicroEmbranchement } from 'app/entities/classificationMS/micro-embranchement/micro-embranchement.model';
import { ISousDivision } from 'app/entities/classificationMS/sous-division/sous-division.model';

export interface IInfraEmbranchement {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  microEmbranchements?: IMicroEmbranchement[] | null;
  synonymes?: IInfraEmbranchement[] | null;
  sousDivision?: ISousDivision | null;
  infraEmbranchement?: IInfraEmbranchement | null;
}

export class InfraEmbranchement implements IInfraEmbranchement {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public microEmbranchements?: IMicroEmbranchement[] | null,
    public synonymes?: IInfraEmbranchement[] | null,
    public sousDivision?: ISousDivision | null,
    public infraEmbranchement?: IInfraEmbranchement | null
  ) {}
}

export function getInfraEmbranchementIdentifier(infraEmbranchement: IInfraEmbranchement): number | undefined {
  return infraEmbranchement.id;
}
