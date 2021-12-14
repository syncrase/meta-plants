import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISuperOrdre, getSuperOrdreIdentifier } from '../super-ordre.model';

export type EntityResponseType = HttpResponse<ISuperOrdre>;
export type EntityArrayResponseType = HttpResponse<ISuperOrdre[]>;

@Injectable({ providedIn: 'root' })
export class SuperOrdreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/super-ordres', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(superOrdre: ISuperOrdre): Observable<EntityResponseType> {
    return this.http.post<ISuperOrdre>(this.resourceUrl, superOrdre, { observe: 'response' });
  }

  update(superOrdre: ISuperOrdre): Observable<EntityResponseType> {
    return this.http.put<ISuperOrdre>(`${this.resourceUrl}/${getSuperOrdreIdentifier(superOrdre) as number}`, superOrdre, {
      observe: 'response',
    });
  }

  partialUpdate(superOrdre: ISuperOrdre): Observable<EntityResponseType> {
    return this.http.patch<ISuperOrdre>(`${this.resourceUrl}/${getSuperOrdreIdentifier(superOrdre) as number}`, superOrdre, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISuperOrdre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISuperOrdre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSuperOrdreToCollectionIfMissing(
    superOrdreCollection: ISuperOrdre[],
    ...superOrdresToCheck: (ISuperOrdre | null | undefined)[]
  ): ISuperOrdre[] {
    const superOrdres: ISuperOrdre[] = superOrdresToCheck.filter(isPresent);
    if (superOrdres.length > 0) {
      const superOrdreCollectionIdentifiers = superOrdreCollection.map(superOrdreItem => getSuperOrdreIdentifier(superOrdreItem)!);
      const superOrdresToAdd = superOrdres.filter(superOrdreItem => {
        const superOrdreIdentifier = getSuperOrdreIdentifier(superOrdreItem);
        if (superOrdreIdentifier == null || superOrdreCollectionIdentifiers.includes(superOrdreIdentifier)) {
          return false;
        }
        superOrdreCollectionIdentifiers.push(superOrdreIdentifier);
        return true;
      });
      return [...superOrdresToAdd, ...superOrdreCollection];
    }
    return superOrdreCollection;
  }
}
