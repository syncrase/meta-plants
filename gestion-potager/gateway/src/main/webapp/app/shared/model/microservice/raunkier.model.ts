export interface IRaunkier {
  id?: number;
  type?: string;
}

export class Raunkier implements IRaunkier {
  constructor(public id?: number, public type?: string) {}
}
