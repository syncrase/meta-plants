export interface IAllelopathie {
  id?: number;
  type?: string;
  description?: string;
  cibleId?: number;
  origineId?: number;
  planteId?: number;
}

export class Allelopathie implements IAllelopathie {
  constructor(
    public id?: number,
    public type?: string,
    public description?: string,
    public cibleId?: number,
    public origineId?: number,
    public planteId?: number
  ) {}
}
