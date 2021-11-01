import { IRessemblance } from 'app/shared/model/microservice/ressemblance.model';
import { IAllelopathie } from 'app/shared/model/microservice/allelopathie.model';
import { INomVernaculaire } from 'app/shared/model/microservice/nom-vernaculaire.model';

export interface IPlante {
  id?: number;
  nomLatin?: string;
  entretien?: string;
  histoire?: string;
  exposition?: string;
  rusticite?: string;
  cycleDeVieId?: number;
  classificationId?: number;
  confusions?: IRessemblance[];
  interactions?: IAllelopathie[];
  nomsVernaculaires?: INomVernaculaire[];
  allelopathieRecueId?: number;
  allelopathieProduiteId?: number;
}

export class Plante implements IPlante {
  constructor(
    public id?: number,
    public nomLatin?: string,
    public entretien?: string,
    public histoire?: string,
    public exposition?: string,
    public rusticite?: string,
    public cycleDeVieId?: number,
    public classificationId?: number,
    public confusions?: IRessemblance[],
    public interactions?: IAllelopathie[],
    public nomsVernaculaires?: INomVernaculaire[],
    public allelopathieRecueId?: number,
    public allelopathieProduiteId?: number
  ) {}
}
