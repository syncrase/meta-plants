import { IFamille } from 'app/entities/classificationMS/famille/famille.model';
import { IMicroOrdre } from 'app/entities/classificationMS/micro-ordre/micro-ordre.model';

export interface ISuperFamille {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  familles?: IFamille[] | null;
  synonymes?: ISuperFamille[] | null;
  microOrdre?: IMicroOrdre | null;
  superFamille?: ISuperFamille | null;
}

export class SuperFamille implements ISuperFamille {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public familles?: IFamille[] | null,
    public synonymes?: ISuperFamille[] | null,
    public microOrdre?: IMicroOrdre | null,
    public superFamille?: ISuperFamille | null
  ) {}
}

export function getSuperFamilleIdentifier(superFamille: ISuperFamille): number | undefined {
  return superFamille.id;
}
