import { IMicroOrdre } from 'app/entities/classificationMS/micro-ordre/micro-ordre.model';
import { ISousOrdre } from 'app/entities/classificationMS/sous-ordre/sous-ordre.model';

export interface IInfraOrdre {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  microOrdres?: IMicroOrdre[] | null;
  synonymes?: IInfraOrdre[] | null;
  sousOrdre?: ISousOrdre | null;
  infraOrdre?: IInfraOrdre | null;
}

export class InfraOrdre implements IInfraOrdre {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public microOrdres?: IMicroOrdre[] | null,
    public synonymes?: IInfraOrdre[] | null,
    public sousOrdre?: ISousOrdre | null,
    public infraOrdre?: IInfraOrdre | null
  ) {}
}

export function getInfraOrdreIdentifier(infraOrdre: IInfraOrdre): number | undefined {
  return infraOrdre.id;
}
