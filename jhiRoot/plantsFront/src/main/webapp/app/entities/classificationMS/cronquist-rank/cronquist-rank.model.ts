import { IUrl } from 'app/entities/classificationMS/url/url.model';
import { CronquistTaxonomikRanks } from 'app/entities/enumerations/cronquist-taxonomik-ranks.model';

export interface ICronquistRank {
  id?: number;
  rank?: CronquistTaxonomikRanks;
  nomFr?: string | null;
  nomLantin?: string | null;
  children?: ICronquistRank[] | null;
  urls?: IUrl[] | null;
  synonymes?: ICronquistRank[] | null;
  parent?: ICronquistRank | null;
  cronquistRank?: ICronquistRank | null;
}

export class CronquistRank implements ICronquistRank {
  constructor(
    public id?: number,
    public rank?: CronquistTaxonomikRanks,
    public nomFr?: string | null,
    public nomLantin?: string | null,
    public children?: ICronquistRank[] | null,
    public urls?: IUrl[] | null,
    public synonymes?: ICronquistRank[] | null,
    public parent?: ICronquistRank | null,
    public cronquistRank?: ICronquistRank | null
  ) {}
}

export function getCronquistRankIdentifier(cronquistRank: ICronquistRank): number | undefined {
  return cronquistRank.id;
}
