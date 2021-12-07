import { IPeriodeAnnee } from 'app/entities/microservice/periode-annee/periode-annee.model';
import { ITypeSemis } from 'app/entities/microservice/type-semis/type-semis.model';
import { IGermination } from 'app/entities/microservice/germination/germination.model';

export interface ISemis {
  id?: number;
  semisPleineTerre?: IPeriodeAnnee | null;
  semisSousAbris?: IPeriodeAnnee | null;
  typeSemis?: ITypeSemis | null;
  germination?: IGermination | null;
}

export class Semis implements ISemis {
  constructor(
    public id?: number,
    public semisPleineTerre?: IPeriodeAnnee | null,
    public semisSousAbris?: IPeriodeAnnee | null,
    public typeSemis?: ITypeSemis | null,
    public germination?: IGermination | null
  ) {}
}

export function getSemisIdentifier(semis: ISemis): number | undefined {
  return semis.id;
}
