import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISuperRegne, getSuperRegneIdentifier } from '../super-regne.model';

export type EntityResponseType = HttpResponse<ISuperRegne>;
export type EntityArrayResponseType = HttpResponse<ISuperRegne[]>;

@Injectable({ providedIn: 'root' })
export class SuperRegneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/super-regnes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(superRegne: ISuperRegne): Observable<EntityResponseType> {
    return this.http.post<ISuperRegne>(this.resourceUrl, superRegne, { observe: 'response' });
  }

  update(superRegne: ISuperRegne): Observable<EntityResponseType> {
    return this.http.put<ISuperRegne>(`${this.resourceUrl}/${getSuperRegneIdentifier(superRegne) as number}`, superRegne, {
      observe: 'response',
    });
  }

  partialUpdate(superRegne: ISuperRegne): Observable<EntityResponseType> {
    return this.http.patch<ISuperRegne>(`${this.resourceUrl}/${getSuperRegneIdentifier(superRegne) as number}`, superRegne, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISuperRegne>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISuperRegne[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSuperRegneToCollectionIfMissing(
    superRegneCollection: ISuperRegne[],
    ...superRegnesToCheck: (ISuperRegne | null | undefined)[]
  ): ISuperRegne[] {
    const superRegnes: ISuperRegne[] = superRegnesToCheck.filter(isPresent);
    if (superRegnes.length > 0) {
      const superRegneCollectionIdentifiers = superRegneCollection.map(superRegneItem => getSuperRegneIdentifier(superRegneItem)!);
      const superRegnesToAdd = superRegnes.filter(superRegneItem => {
        const superRegneIdentifier = getSuperRegneIdentifier(superRegneItem);
        if (superRegneIdentifier == null || superRegneCollectionIdentifiers.includes(superRegneIdentifier)) {
          return false;
        }
        superRegneCollectionIdentifiers.push(superRegneIdentifier);
        return true;
      });
      return [...superRegnesToAdd, ...superRegneCollection];
    }
    return superRegneCollection;
  }
}
