export interface IAPGIIPlante {
  id?: number;
  ordre?: string;
  famille?: string;
}

export class APGIIPlante implements IAPGIIPlante {
  constructor(public id?: number, public ordre?: string, public famille?: string) {}
}

export function getAPGIIPlanteIdentifier(aPGIIPlante: IAPGIIPlante): number | undefined {
  return aPGIIPlante.id;
}
