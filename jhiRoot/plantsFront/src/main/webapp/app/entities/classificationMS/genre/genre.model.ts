import { ISousGenre } from 'app/entities/classificationMS/sous-genre/sous-genre.model';
import { ISousTribu } from 'app/entities/classificationMS/sous-tribu/sous-tribu.model';

export interface IGenre {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  sousGenres?: ISousGenre[] | null;
  synonymes?: IGenre[] | null;
  sousTribu?: ISousTribu | null;
  genre?: IGenre | null;
}

export class Genre implements IGenre {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public sousGenres?: ISousGenre[] | null,
    public synonymes?: IGenre[] | null,
    public sousTribu?: ISousTribu | null,
    public genre?: IGenre | null
  ) {}
}

export function getGenreIdentifier(genre: IGenre): number | undefined {
  return genre.id;
}
