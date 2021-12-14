import { ISousOrdre } from 'app/entities/classificationMS/sous-ordre/sous-ordre.model';
import { ISuperOrdre } from 'app/entities/classificationMS/super-ordre/super-ordre.model';

export interface IOrdre {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousOrdres?: ISousOrdre[] | null;
  synonymes?: IOrdre[] | null;
  superOrdre?: ISuperOrdre | null;
  ordre?: IOrdre | null;
}

export class Ordre implements IOrdre {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousOrdres?: ISousOrdre[] | null,
    public synonymes?: IOrdre[] | null,
    public superOrdre?: ISuperOrdre | null,
    public ordre?: IOrdre | null
  ) {}
}

export function getOrdreIdentifier(ordre: IOrdre): number | undefined {
  return ordre.id;
}
