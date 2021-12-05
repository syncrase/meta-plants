export interface ITypeSemis {
  id?: number;
  description?: string | null;
}

export class TypeSemis implements ITypeSemis {
  constructor(public id?: number, public description?: string | null) {}
}

export function getTypeSemisIdentifier(typeSemis: ITypeSemis): number | undefined {
  return typeSemis.id;
}
