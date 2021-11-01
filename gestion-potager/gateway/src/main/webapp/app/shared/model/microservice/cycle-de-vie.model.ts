export interface ICycleDeVie {
  id?: number;
  vitesseDeCroissance?: string;
  semisId?: number;
  apparitionFeuillesId?: number;
  floraisonId?: number;
  recolteId?: number;
  croissanceId?: number;
  maturiteId?: number;
  plantationId?: number;
  rempotageId?: number;
}

export class CycleDeVie implements ICycleDeVie {
  constructor(
    public id?: number,
    public vitesseDeCroissance?: string,
    public semisId?: number,
    public apparitionFeuillesId?: number,
    public floraisonId?: number,
    public recolteId?: number,
    public croissanceId?: number,
    public maturiteId?: number,
    public plantationId?: number,
    public rempotageId?: number
  ) {}
}
