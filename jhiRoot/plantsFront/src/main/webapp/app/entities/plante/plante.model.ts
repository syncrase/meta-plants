import { ICycleDeVie } from 'app/entities/cycle-de-vie/cycle-de-vie.model';
import { IRessemblance } from 'app/entities/ressemblance/ressemblance.model';
import { IEnsoleillement } from 'app/entities/ensoleillement/ensoleillement.model';
import { ISol } from 'app/entities/sol/sol.model';
import { IClassification } from 'app/entities/classification/classification.model';
import { INomVernaculaire } from 'app/entities/nom-vernaculaire/nom-vernaculaire.model';
import { ITemperature } from 'app/entities/temperature/temperature.model';
import { IRacine } from 'app/entities/racine/racine.model';
import { IStrate } from 'app/entities/strate/strate.model';
import { IFeuillage } from 'app/entities/feuillage/feuillage.model';

export interface IPlante {
  id?: number;
  entretien?: string | null;
  histoire?: string | null;
  vitesseCroissance?: string | null;
  exposition?: string | null;
  cycleDeVie?: ICycleDeVie | null;
  confusions?: IRessemblance[] | null;
  ensoleillements?: IEnsoleillement[] | null;
  sols?: ISol[] | null;
  classification?: IClassification | null;
  nomsVernaculaires?: INomVernaculaire[] | null;
  temperature?: ITemperature | null;
  racine?: IRacine | null;
  strate?: IStrate | null;
  feuillage?: IFeuillage | null;
}

export class Plante implements IPlante {
  constructor(
    public id?: number,
    public entretien?: string | null,
    public histoire?: string | null,
    public vitesseCroissance?: string | null,
    public exposition?: string | null,
    public cycleDeVie?: ICycleDeVie | null,
    public confusions?: IRessemblance[] | null,
    public ensoleillements?: IEnsoleillement[] | null,
    public sols?: ISol[] | null,
    public classification?: IClassification | null,
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
