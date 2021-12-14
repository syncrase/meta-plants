import { IClassification } from 'app/entities/plantsMS/classification/classification.model';
import { IRessemblance } from 'app/entities/plantsMS/ressemblance/ressemblance.model';
import { IEnsoleillement } from 'app/entities/plantsMS/ensoleillement/ensoleillement.model';
import { ICycleDeVie } from 'app/entities/plantsMS/cycle-de-vie/cycle-de-vie.model';
import { ISol } from 'app/entities/plantsMS/sol/sol.model';
import { ITemperature } from 'app/entities/plantsMS/temperature/temperature.model';
import { IRacine } from 'app/entities/plantsMS/racine/racine.model';
import { IStrate } from 'app/entities/plantsMS/strate/strate.model';
import { IFeuillage } from 'app/entities/plantsMS/feuillage/feuillage.model';
import { INomVernaculaire } from 'app/entities/plantsMS/nom-vernaculaire/nom-vernaculaire.model';

export interface IPlante {
  id?: number;
  entretien?: string | null;
  histoire?: string | null;
  vitesseCroissance?: string | null;
  exposition?: string | null;
  classification?: IClassification | null;
  confusions?: IRessemblance[] | null;
  ensoleillements?: IEnsoleillement[] | null;
  plantesPotageres?: IPlante[] | null;
  cycleDeVie?: ICycleDeVie | null;
  sol?: ISol | null;
  temperature?: ITemperature | null;
  racine?: IRacine | null;
  strate?: IStrate | null;
  feuillage?: IFeuillage | null;
  nomsVernaculaires?: INomVernaculaire[] | null;
  plante?: IPlante | null;
}

export class Plante implements IPlante {
  constructor(
    public id?: number,
    public entretien?: string | null,
    public histoire?: string | null,
    public vitesseCroissance?: string | null,
    public exposition?: string | null,
    public classification?: IClassification | null,
    public confusions?: IRessemblance[] | null,
    public ensoleillements?: IEnsoleillement[] | null,
    public plantesPotageres?: IPlante[] | null,
    public cycleDeVie?: ICycleDeVie | null,
    public sol?: ISol | null,
    public temperature?: ITemperature | null,
    public racine?: IRacine | null,
    public strate?: IStrate | null,
    public feuillage?: IFeuillage | null,
    public nomsVernaculaires?: INomVernaculaire[] | null,
    public plante?: IPlante | null
  ) {}
}

export function getPlanteIdentifier(plante: IPlante): number | undefined {
  return plante.id;
}
