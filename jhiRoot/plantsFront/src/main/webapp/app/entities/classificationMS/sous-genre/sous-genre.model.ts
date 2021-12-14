import { ISection } from 'app/entities/classificationMS/section/section.model';
import { IGenre } from 'app/entities/classificationMS/genre/genre.model';

export interface ISousGenre {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sections?: ISection[] | null;
  synonymes?: ISousGenre[] | null;
  genre?: IGenre | null;
  sousGenre?: ISousGenre | null;
}

export class SousGenre implements ISousGenre {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sections?: ISection[] | null,
    public synonymes?: ISousGenre[] | null,
    public genre?: IGenre | null,
    public sousGenre?: ISousGenre | null
  ) {}
}

export function getSousGenreIdentifier(sousGenre: ISousGenre): number | undefined {
  return sousGenre.id;
}
