export interface IAPGI {
  id?: number;
  ordre?: string;
  famille?: string;
}

export class APGI implements IAPGI {
  constructor(public id?: number, public ordre?: string, public famille?: string) {}
}

export function getAPGIIdentifier(aPGI: IAPGI): number | undefined {
  return aPGI.id;
}
