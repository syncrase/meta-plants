export interface ITypeSemis {
  id?: number;
  description?: string;
}

export class TypeSemis implements ITypeSemis {
  constructor(public id?: number, public description?: string) {}
}
