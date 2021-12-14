import { IEspece } from 'app/entities/classificationMS/espece/espece.model';
import { ISection } from 'app/entities/classificationMS/section/section.model';

export interface ISousSection {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  especes?: IEspece[] | null;
  synonymes?: ISousSection[] | null;
  section?: ISection | null;
  sousSection?: ISousSection | null;
}

export class SousSection implements ISousSection {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public especes?: IEspece[] | null,
    public synonymes?: ISousSection[] | null,
    public section?: ISection | null,
    public sousSection?: ISousSection | null
  ) {}
}

export function getSousSectionIdentifier(sousSection: ISousSection): number | undefined {
  return sousSection.id;
}
