export interface IClassification {
  id?: number;
  raunkierId?: number;
  cronquistId?: number;
  apg1Id?: number;
  apg2Id?: number;
  apg3Id?: number;
  apg4Id?: number;
}

export class Classification implements IClassification {
  constructor(
    public id?: number,
    public raunkierId?: number,
    public cronquistId?: number,
    public apg1Id?: number,
    public apg2Id?: number,
    public apg3Id?: number,
    public apg4Id?: number
  ) {}
}
