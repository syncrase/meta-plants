export interface ICronquist {
  id?: number;
  regne?: string | null;
  sousRegne?: string | null;
  division?: string | null;
  classe?: string | null;
  sousClasse?: string | null;
  ordre?: string | null;
  famille?: string | null;
  genre?: string | null;
  espece?: string | null;
}

export class Cronquist implements ICronquist {
  constructor(
    public id?: number,
    public regne?: string | null,
    public sousRegne?: string | null,
    public division?: string | null,
    public classe?: string | null,
    public sousClasse?: string | null,
    public ordre?: string | null,
    public famille?: string | null,
    public genre?: string | null,
    public espece?: string | null
  ) {}
}

export function getCronquistIdentifier(cronquist: ICronquist): number | undefined {
  return cronquist.id;
}
