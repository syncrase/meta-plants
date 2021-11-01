export interface ISol {
  id?: number;
  acidite?: number;
  type?: string;
}

export class Sol implements ISol {
  constructor(public id?: number, public acidite?: number, public type?: string) {}
}
