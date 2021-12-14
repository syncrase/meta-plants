import { ISuperDivision } from 'app/entities/classificationMS/super-division/super-division.model';
import { IRameau } from 'app/entities/classificationMS/rameau/rameau.model';

export interface IInfraRegne {
  id?: number;
  nomFr?: string;
  nomLatin?: string | null;
  superDivisions?: ISuperDivision[] | null;
  synonymes?: IInfraRegne[] | null;
  rameau?: IRameau | null;
  infraRegne?: IInfraRegne | null;
}

export class InfraRegne implements IInfraRegne {
  constructor(
    public id?: number,
    public nomFr?: string,
    public nomLatin?: string | null,
    public superDivisions?: ISuperDivision[] | null,
    public synonymes?: IInfraRegne[] | null,
    public rameau?: IRameau | null,
    public infraRegne?: IInfraRegne | null
  ) {}
}

export function getInfraRegneIdentifier(infraRegne: IInfraRegne): number | undefined {
  return infraRegne.id;
}
