export interface IAPGIPlante {
  id?: number;
  ordre?: string;
  famille?: string;
}

export class APGIPlante implements IAPGIPlante {
  constructor(public id?: number, public ordre?: string, public famille?: string) {}
}

export function getAPGIPlanteIdentifier(aPGIPlante: IAPGIPlante): number | undefined {
  return aPGIPlante.id;
}
