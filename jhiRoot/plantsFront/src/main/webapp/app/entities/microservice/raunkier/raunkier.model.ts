export interface IRaunkier {
  id?: number;
  type?: string;
}

export class Raunkier implements IRaunkier {
  constructor(public id?: number, public type?: string) {}
}

export function getRaunkierIdentifier(raunkier: IRaunkier): number | undefined {
  return raunkier.id;
}
