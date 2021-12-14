import { ISousSection } from 'app/entities/classificationMS/sous-section/sous-section.model';
import { ISousGenre } from 'app/entities/classificationMS/sous-genre/sous-genre.model';

export interface ISection {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousSections?: ISousSection[] | null;
  synonymes?: ISection[] | null;
  sousGenre?: ISousGenre | null;
  section?: ISection | null;
}

export class Section implements ISection {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousSections?: ISousSection[] | null,
    public synonymes?: ISection[] | null,
    public sousGenre?: ISousGenre | null,
    public section?: ISection | null
  ) {}
}

export function getSectionIdentifier(section: ISection): number | undefined {
  return section.id;
}
