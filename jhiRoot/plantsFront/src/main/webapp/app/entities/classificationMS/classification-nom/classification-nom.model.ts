import { ICronquistRank } from 'app/entities/classificationMS/cronquist-rank/cronquist-rank.model';

export interface IClassificationNom {
  id?: number;
  nomFr?: string | null;
  nomLatin?: string | null;
  cronquistRank?: ICronquistRank;
}

export class ClassificationNom implements IClassificationNom {
  constructor(public id?: number, public nomFr?: string | null, public nomLatin?: string | null, public cronquistRank?: ICronquistRank) {}
}

export function getClassificationNomIdentifier(classificationNom: IClassificationNom): number | undefined {
  return classificationNom.id;
}
