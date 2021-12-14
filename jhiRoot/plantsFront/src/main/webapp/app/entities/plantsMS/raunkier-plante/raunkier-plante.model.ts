export interface IRaunkierPlante {
  id?: number;
  type?: string;
}

export class RaunkierPlante implements IRaunkierPlante {
  constructor(public id?: number, public type?: string) {}
}

export function getRaunkierPlanteIdentifier(raunkierPlante: IRaunkierPlante): number | undefined {
  return raunkierPlante.id;
}
