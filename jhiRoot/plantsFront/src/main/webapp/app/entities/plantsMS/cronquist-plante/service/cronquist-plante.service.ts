import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICronquistPlante, getCronquistPlanteIdentifier } from '../cronquist-plante.model';

export type EntityResponseType = HttpResponse<ICronquistPlante>;
export type EntityArrayResponseType = HttpResponse<ICronquistPlante[]>;

@Injectable({ providedIn: 'root' })
export class CronquistPlanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cronquist-plantes', 'plantsms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cronquistPlante: ICronquistPlante): Observable<EntityResponseType> {
    return this.http.post<ICronquistPlante>(this.resourceUrl, cronquistPlante, { observe: 'response' });
  }

  update(cronquistPlante: ICronquistPlante): Observable<EntityResponseType> {
    return this.http.put<ICronquistPlante>(
      `${this.resourceUrl}/${getCronquistPlanteIdentifier(cronquistPlante) as number}`,
      cronquistPlante,
      { observe: 'response' }
    );
  }

  partialUpdate(cronquistPlante: ICronquistPlante): Observable<EntityResponseType> {
    return this.http.patch<ICronquistPlante>(
      `${this.resourceUrl}/${getCronquistPlanteIdentifier(cronquistPlante) as number}`,
      cronquistPlante,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICronquistPlante>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICronquistPlante[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCronquistPlanteToCollectionIfMissing(
    cronquistPlanteCollection: ICronquistPlante[],
    ...cronquistPlantesToCheck: (ICronquistPlante | null | undefined)[]
  ): ICronquistPlante[] {
    const cronquistPlantes: ICronquistPlante[] = cronquistPlantesToCheck.filter(isPresent);
    if (cronquistPlantes.length > 0) {
      const cronquistPlanteCollectionIdentifiers = cronquistPlanteCollection.map(
        cronquistPlanteItem => getCronquistPlanteIdentifier(cronquistPlanteItem)!
      );
      const cronquistPlantesToAdd = cronquistPlantes.filter(cronquistPlanteItem => {
        const cronquistPlanteIdentifier = getCronquistPlanteIdentifier(cronquistPlanteItem);
        if (cronquistPlanteIdentifier == null || cronquistPlanteCollectionIdentifiers.includes(cronquistPlanteIdentifier)) {
          return false;
        }
        cronquistPlanteCollectionIdentifiers.push(cronquistPlanteIdentifier);
        return true;
      });
      return [...cronquistPlantesToAdd, ...cronquistPlanteCollection];
    }
    return cronquistPlanteCollection;
  }
}
