import { IClade } from 'app/entities/plantsMS/clade/clade.model';

export interface IAPGIIIPlante {
  id?: number;
  ordre?: string;
  famille?: string;
  sousFamille?: string;
  tribu?: string | null;
  sousTribu?: string | null;
  genre?: string;
  clades?: IClade[] | null;
}

export class APGIIIPlante implements IAPGIIIPlante {
  constructor(
    public id?: number,
    public ordre?: string,
    public famille?: string,
    public sousFamille?: string,
    public tribu?: string | null,
    public sousTribu?: string | null,
    public genre?: string,
    public clades?: IClade[] | null
  ) {}
}

export function getAPGIIIPlanteIdentifier(aPGIIIPlante: IAPGIIIPlante): number | undefined {
  return aPGIIIPlante.id;
}
