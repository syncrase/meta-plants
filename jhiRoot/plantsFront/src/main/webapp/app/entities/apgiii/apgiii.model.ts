export interface IAPGIII {
  id?: number;
  ordre?: string;
  famille?: string;
}

export class APGIII implements IAPGIII {
  constructor(public id?: number, public ordre?: string, public famille?: string) {}
}

export function getAPGIIIIdentifier(aPGIII: IAPGIII): number | undefined {
  return aPGIII.id;
}