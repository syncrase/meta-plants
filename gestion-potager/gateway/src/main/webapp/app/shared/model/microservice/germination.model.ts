export interface IGermination {
  id?: number;
  tempsDeGermination?: string;
  conditionDeGermination?: string;
}

export class Germination implements IGermination {
  constructor(public id?: number, public tempsDeGermination?: string, public conditionDeGermination?: string) {}
}
