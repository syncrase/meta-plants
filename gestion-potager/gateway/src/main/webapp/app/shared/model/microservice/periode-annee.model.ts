export interface IPeriodeAnnee {
  id?: number;
  debutNom?: string;
  debutId?: number;
  finNom?: string;
  finId?: number;
}

export class PeriodeAnnee implements IPeriodeAnnee {
  constructor(public id?: number, public debutNom?: string, public debutId?: number, public finNom?: string, public finId?: number) {}
}
