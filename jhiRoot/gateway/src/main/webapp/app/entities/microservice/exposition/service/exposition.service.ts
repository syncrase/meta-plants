import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExposition, getExpositionIdentifier } from '../exposition.model';

export type EntityResponseType = HttpResponse<IExposition>;
export type EntityArrayResponseType = HttpResponse<IExposition[]>;

@Injectable({ providedIn: 'root' })
export class ExpositionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expositions', 'microservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(exposition: IExposition): Observable<EntityResponseType> {
    return this.http.post<IExposition>(this.resourceUrl, exposition, { observe: 'response' });
  }

  update(exposition: IExposition): Observable<EntityResponseType> {
    return this.http.put<IExposition>(`${this.resourceUrl}/${getExpositionIdentifier(exposition) as number}`, exposition, {
      observe: 'response',
    });
  }

  partialUpdate(exposition: IExposition): Observable<EntityResponseType> {
    return this.http.patch<IExposition>(`${this.resourceUrl}/${getExpositionIdentifier(exposition) as number}`, exposition, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExposition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExposition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExpositionToCollectionIfMissing(
    expositionCollection: IExposition[],
    ...expositionsToCheck: (IExposition | null | undefined)[]
  ): IExposition[] {
    const expositions: IExposition[] = expositionsToCheck.filter(isPresent);
    if (expositions.length > 0) {
      const expositionCollectionIdentifiers = expositionCollection.map(expositionItem => getExpositionIdentifier(expositionItem)!);
      const expositionsToAdd = expositions.filter(expositionItem => {
        const expositionIdentifier = getExpositionIdentifier(expositionItem);
        if (expositionIdentifier == null || expositionCollectionIdentifiers.includes(expositionIdentifier)) {
          return false;
        }
        expositionCollectionIdentifiers.push(expositionIdentifier);
        return true;
      });
      return [...expositionsToAdd, ...expositionCollection];
    }
    return expositionCollection;
  }
}
