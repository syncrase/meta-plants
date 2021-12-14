import { ISuperFamille } from 'app/entities/classificationMS/super-famille/super-famille.model';
import { IInfraOrdre } from 'app/entities/classificationMS/infra-ordre/infra-ordre.model';

export interface IMicroOrdre {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  superFamilles?: ISuperFamille[] | null;
  synonymes?: IMicroOrdre[] | null;
  infraOrdre?: IInfraOrdre | null;
  microOrdre?: IMicroOrdre | null;
}

export class MicroOrdre implements IMicroOrdre {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public superFamilles?: ISuperFamille[] | null,
    public synonymes?: IMicroOrdre[] | null,
    public infraOrdre?: IInfraOrdre | null,
    public microOrdre?: IMicroOrdre | null
  ) {}
}

export function getMicroOrdreIdentifier(microOrdre: IMicroOrdre): number | undefined {
  return microOrdre.id;
}
