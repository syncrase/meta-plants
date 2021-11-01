export interface ICronquist {
  id?: number;
  regne?: string;
  sousRegne?: string;
  division?: string;
  classe?: string;
  sousClasse?: string;
  ordre?: string;
  famille?: string;
  genre?: string;
}

export class Cronquist implements ICronquist {
  constructor(
    public id?: number,
    public regne?: string,
    public sousRegne?: string,
    public division?: string,
    public classe?: string,
    public sousClasse?: string,
    public ordre?: string,
    public famille?: string,
    public genre?: string
  ) {}
}
