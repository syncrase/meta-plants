export interface IAPGIVPlante {
  id?: number;
  ordre?: string;
  famille?: string;
}

export class APGIVPlante implements IAPGIVPlante {
  constructor(public id?: number, public ordre?: string, public famille?: string) {}
}

export function getAPGIVPlanteIdentifier(aPGIVPlante: IAPGIVPlante): number | undefined {
  return aPGIVPlante.id;
}
