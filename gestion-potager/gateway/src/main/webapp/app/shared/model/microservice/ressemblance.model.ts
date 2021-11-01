export interface IRessemblance {
  id?: number;
  description?: string;
  confusionId?: number;
}

export class Ressemblance implements IRessemblance {
  constructor(public id?: number, public description?: string, public confusionId?: number) {}
}
