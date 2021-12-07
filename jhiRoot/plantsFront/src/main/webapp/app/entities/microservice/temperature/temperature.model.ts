import { IPlante } from 'app/entities/microservice/plante/plante.model';

export interface ITemperature {
  id?: number;
  min?: number | null;
  max?: number | null;
  description?: string | null;
  rusticite?: string | null;
  plantes?: IPlante[] | null;
}

export class Temperature implements ITemperature {
  constructor(
    public id?: number,
    public min?: number | null,
    public max?: number | null,
    public description?: string | null,
    public rusticite?: string | null,
    public plantes?: IPlante[] | null
  ) {}
}

export function getTemperatureIdentifier(temperature: ITemperature): number | undefined {
  return temperature.id;
}
