import { IUrl } from 'app/entities/classificationMS/url/url.model';
import { IClassificationNom } from 'app/entities/classificationMS/classification-nom/classification-nom.model';
import { CronquistTaxonomikRanks } from 'app/entities/enumerations/cronquist-taxonomik-ranks.model';

export interface ICronquistRank {
  id?: number;
  rank?: CronquistTaxonomikRanks;
  children?: ICronquistRank[] | null;
  urls?: IUrl[] | null;
  noms?: IClassificationNom[];
  parent?: ICronquistRank | null;
}

export class CronquistRank implements ICronquistRank {
  constructor(
    public id?: number,
    public rank?: CronquistTaxonomikRanks,
    public children?: ICronquistRank[] | null,
    public urls?: IUrl[] | null,
    public noms?: IClassificationNom[],
    public parent?: ICronquistRank | null
  ) {}
}

export function getCronquistRankIdentifier(cronquistRank: ICronquistRank): number | undefined {
  return cronquistRank.id;
}
