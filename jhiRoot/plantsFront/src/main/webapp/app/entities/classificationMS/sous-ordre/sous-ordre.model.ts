import { IInfraOrdre } from 'app/entities/classificationMS/infra-ordre/infra-ordre.model';
import { IOrdre } from 'app/entities/classificationMS/ordre/ordre.model';

export interface ISousOrdre {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  infraOrdres?: IInfraOrdre[] | null;
  synonymes?: ISousOrdre[] | null;
  ordre?: IOrdre | null;
  sousOrdre?: ISousOrdre | null;
}

export class SousOrdre implements ISousOrdre {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public infraOrdres?: IInfraOrdre[] | null,
    public synonymes?: ISousOrdre[] | null,
    public ordre?: IOrdre | null,
    public sousOrdre?: ISousOrdre | null
  ) {}
}

export function getSousOrdreIdentifier(sousOrdre: ISousOrdre): number | undefined {
  return sousOrdre.id;
}
