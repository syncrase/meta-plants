import { IPlante } from 'app/entities/plantsMS/plante/plante.model';

export interface IClassificationCronquist {
  id?: number;
  superRegne?: string | null;
  regne?: string | null;
  sousRegne?: string | null;
  rameau?: string | null;
  infraRegne?: string | null;
  superEmbranchement?: string | null;
  division?: string | null;
  sousEmbranchement?: string | null;
  infraEmbranchement?: string | null;
  microEmbranchement?: string | null;
  superClasse?: string | null;
  classe?: string | null;
  sousClasse?: string | null;
  infraClasse?: string | null;
  superOrdre?: string | null;
  ordre?: string | null;
  sousOrdre?: string | null;
  infraOrdre?: string | null;
  microOrdre?: string | null;
  superFamille?: string | null;
  famille?: string | null;
  sousFamille?: string | null;
  tribu?: string | null;
  sousTribu?: string | null;
  genre?: string | null;
  sousGenre?: string | null;
  section?: string | null;
  sousSection?: string | null;
  espece?: string | null;
  sousEspece?: string | null;
  variete?: string | null;
  sousVariete?: string | null;
  forme?: string | null;
  plante?: IPlante | null;
}

export class ClassificationCronquist implements IClassificationCronquist {
  constructor(
    public id?: number,
    public superRegne?: string | null,
    public regne?: string | null,
    public sousRegne?: string | null,
    public rameau?: string | null,
    public infraRegne?: string | null,
    public superEmbranchement?: string | null,
    public division?: string | null,
    public sousEmbranchement?: string | null,
    public infraEmbranchement?: string | null,
    public microEmbranchement?: string | null,
    public superClasse?: string | null,
    public classe?: string | null,
    public sousClasse?: string | null,
    public infraClasse?: string | null,
    public superOrdre?: string | null,
    public ordre?: string | null,
    public sousOrdre?: string | null,
    public infraOrdre?: string | null,
    public microOrdre?: string | null,
    public superFamille?: string | null,
    public famille?: string | null,
    public sousFamille?: string | null,
    public tribu?: string | null,
    public sousTribu?: string | null,
    public genre?: string | null,
    public sousGenre?: string | null,
    public section?: string | null,
    public sousSection?: string | null,
    public espece?: string | null,
    public sousEspece?: string | null,
    public variete?: string | null,
    public sousVariete?: string | null,
    public forme?: string | null,
    public plante?: IPlante | null
  ) {}
}

export function getClassificationCronquistIdentifier(classificationCronquist: IClassificationCronquist): number | undefined {
  return classificationCronquist.id;
}
