import { ICycleDeVie } from 'app/entities/microservice/cycle-de-vie/cycle-de-vie.model';
import { IClassification } from 'app/entities/microservice/classification/classification.model';
import { IRessemblance } from 'app/entities/microservice/ressemblance/ressemblance.model';
import { IAllelopathie } from 'app/entities/microservice/allelopathie/allelopathie.model';
import { IExposition } from 'app/entities/microservice/exposition/exposition.model';
import { ISol } from 'app/entities/microservice/sol/sol.model';
import { INomVernaculaire } from 'app/entities/microservice/nom-vernaculaire/nom-vernaculaire.model';
import { ITemperature } from 'app/entities/microservice/temperature/temperature.model';
import { IRacine } from 'app/entities/microservice/racine/racine.model';
import { IStrate } from 'app/entities/microservice/strate/strate.model';
import { IFeuillage } from 'app/entities/microservice/feuillage/feuillage.model';

export interface IPlante {
  id?: number;
  nomLatin?: string;
  entretien?: string | null;
  histoire?: string | null;
  vitesse?: string | null;
  cycleDeVie?: ICycleDeVie | null;
  classification?: IClassification | null;
  confusions?: IRessemblance[] | null;
  interactions?: IAllelopathie[] | null;
  expositions?: IExposition[] | null;
  sols?: ISol[] | null;
  nomsVernaculaires?: INomVernaculaire[] | null;
  temperature?: ITemperature | null;
  racine?: IRacine | null;
  strate?: IStrate | null;
  feuillage?: IFeuillage | null;
}

export class Plante implements IPlante {
  constructor(
    public id?: number,
    public nomLatin?: string,
    public entretien?: string | null,
    public histoire?: string | null,
    public vitesse?: string | null,
    public cycleDeVie?: ICycleDeVie | null,
    public classification?: IClassification | null,
    public confusions?: IRessemblance[] | null,
    public interactions?: IAllelopathie[] | null,
    public expositions?: IExposition[] | null,
    public sols?: ISol[] | null,
    public nomsVernaculaires?: INomVernaculaire[] | null,
    public temperature?: ITemperature | null,
    public racine?: IRacine | null,
    public strate?: IStrate | null,
    public feuillage?: IFeuillage | null
  ) {}
}

export function getPlanteIdentifier(plante: IPlante): number | undefined {
  return plante.id;
}
