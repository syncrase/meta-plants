import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRaunkierPlante, getRaunkierPlanteIdentifier } from '../raunkier-plante.model';

export type EntityResponseType = HttpResponse<IRaunkierPlante>;
export type EntityArrayResponseType = HttpResponse<IRaunkierPlante[]>;

@Injectable({ providedIn: 'root' })
export class RaunkierPlanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/raunkier-plantes', 'plantsms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(raunkierPlante: IRaunkierPlante): Observable<EntityResponseType> {
    return this.http.post<IRaunkierPlante>(this.resourceUrl, raunkierPlante, { observe: 'response' });
  }

  update(raunkierPlante: IRaunkierPlante): Observable<EntityResponseType> {
    return this.http.put<IRaunkierPlante>(`${this.resourceUrl}/${getRaunkierPlanteIdentifier(raunkierPlante) as number}`, raunkierPlante, {
      observe: 'response',
    });
  }

  partialUpdate(raunkierPlante: IRaunkierPlante): Observable<EntityResponseType> {
    return this.http.patch<IRaunkierPlante>(
      `${this.resourceUrl}/${getRaunkierPlanteIdentifier(raunkierPlante) as number}`,
      raunkierPlante,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRaunkierPlante>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRaunkierPlante[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRaunkierPlanteToCollectionIfMissing(
    raunkierPlanteCollection: IRaunkierPlante[],
    ...raunkierPlantesToCheck: (IRaunkierPlante | null | undefined)[]
  ): IRaunkierPlante[] {
    const raunkierPlantes: IRaunkierPlante[] = raunkierPlantesToCheck.filter(isPresent);
    if (raunkierPlantes.length > 0) {
      const raunkierPlanteCollectionIdentifiers = raunkierPlanteCollection.map(
        raunkierPlanteItem => getRaunkierPlanteIdentifier(raunkierPlanteItem)!
      );
      const raunkierPlantesToAdd = raunkierPlantes.filter(raunkierPlanteItem => {
        const raunkierPlanteIdentifier = getRaunkierPlanteIdentifier(raunkierPlanteItem);
        if (raunkierPlanteIdentifier == null || raunkierPlanteCollectionIdentifiers.includes(raunkierPlanteIdentifier)) {
          return false;
        }
        raunkierPlanteCollectionIdentifiers.push(raunkierPlanteIdentifier);
        return true;
      });
      return [...raunkierPlantesToAdd, ...raunkierPlanteCollection];
    }
    return raunkierPlanteCollection;
  }
}
