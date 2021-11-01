export interface IAPGII {
  id?: number;
  ordre?: string;
  famille?: string;
}

export class APGII implements IAPGII {
  constructor(public id?: number, public ordre?: string, public famille?: string) {}
}
