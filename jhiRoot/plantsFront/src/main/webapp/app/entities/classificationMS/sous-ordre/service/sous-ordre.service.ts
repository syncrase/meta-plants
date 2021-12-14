import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousOrdre, getSousOrdreIdentifier } from '../sous-ordre.model';

export type EntityResponseType = HttpResponse<ISousOrdre>;
export type EntityArrayResponseType = HttpResponse<ISousOrdre[]>;

@Injectable({ providedIn: 'root' })
export class SousOrdreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-ordres', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousOrdre: ISousOrdre): Observable<EntityResponseType> {
    return this.http.post<ISousOrdre>(this.resourceUrl, sousOrdre, { observe: 'response' });
  }

  update(sousOrdre: ISousOrdre): Observable<EntityResponseType> {
    return this.http.put<ISousOrdre>(`${this.resourceUrl}/${getSousOrdreIdentifier(sousOrdre) as number}`, sousOrdre, {
      observe: 'response',
    });
  }

  partialUpdate(sousOrdre: ISousOrdre): Observable<EntityResponseType> {
    return this.http.patch<ISousOrdre>(`${this.resourceUrl}/${getSousOrdreIdentifier(sousOrdre) as number}`, sousOrdre, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousOrdre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousOrdre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousOrdreToCollectionIfMissing(
    sousOrdreCollection: ISousOrdre[],
    ...sousOrdresToCheck: (ISousOrdre | null | undefined)[]
  ): ISousOrdre[] {
    const sousOrdres: ISousOrdre[] = sousOrdresToCheck.filter(isPresent);
    if (sousOrdres.length > 0) {
      const sousOrdreCollectionIdentifiers = sousOrdreCollection.map(sousOrdreItem => getSousOrdreIdentifier(sousOrdreItem)!);
      const sousOrdresToAdd = sousOrdres.filter(sousOrdreItem => {
        const sousOrdreIdentifier = getSousOrdreIdentifier(sousOrdreItem);
        if (sousOrdreIdentifier == null || sousOrdreCollectionIdentifiers.includes(sousOrdreIdentifier)) {
          return false;
        }
        sousOrdreCollectionIdentifiers.push(sousOrdreIdentifier);
        return true;
      });
      return [...sousOrdresToAdd, ...sousOrdreCollection];
    }
    return sousOrdreCollection;
  }
}
