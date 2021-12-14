import { IGenre } from 'app/entities/classificationMS/genre/genre.model';
import { ITribu } from 'app/entities/classificationMS/tribu/tribu.model';

export interface ISousTribu {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  genres?: IGenre[] | null;
  synonymes?: ISousTribu[] | null;
  tribu?: ITribu | null;
  sousTribu?: ISousTribu | null;
}

export class SousTribu implements ISousTribu {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public genres?: IGenre[] | null,
    public synonymes?: ISousTribu[] | null,
    public tribu?: ITribu | null,
    public sousTribu?: ISousTribu | null
  ) {}
}

export function getSousTribuIdentifier(sousTribu: ISousTribu): number | undefined {
  return sousTribu.id;
}
