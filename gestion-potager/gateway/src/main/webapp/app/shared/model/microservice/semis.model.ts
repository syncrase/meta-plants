export interface ISemis {
  id?: number;
  semisPleineTerreId?: number;
  semisSousAbrisId?: number;
  typeSemisId?: number;
  germinationId?: number;
}

export class Semis implements ISemis {
  constructor(
    public id?: number,
    public semisPleineTerreId?: number,
    public semisSousAbrisId?: number,
    public typeSemisId?: number,
    public germinationId?: number
  ) {}
}
