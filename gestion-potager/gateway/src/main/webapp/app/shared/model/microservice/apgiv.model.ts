export interface IAPGIV {
  id?: number;
  ordre?: string;
  famille?: string;
}

export class APGIV implements IAPGIV {
  constructor(public id?: number, public ordre?: string, public famille?: string) {}
}
