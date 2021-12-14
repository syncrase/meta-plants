import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousRegne, getSousRegneIdentifier } from '../sous-regne.model';

export type EntityResponseType = HttpResponse<ISousRegne>;
export type EntityArrayResponseType = HttpResponse<ISousRegne[]>;

@Injectable({ providedIn: 'root' })
export class SousRegneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-regnes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousRegne: ISousRegne): Observable<EntityResponseType> {
    return this.http.post<ISousRegne>(this.resourceUrl, sousRegne, { observe: 'response' });
  }

  update(sousRegne: ISousRegne): Observable<EntityResponseType> {
    return this.http.put<ISousRegne>(`${this.resourceUrl}/${getSousRegneIdentifier(sousRegne) as number}`, sousRegne, {
      observe: 'response',
    });
  }

  partialUpdate(sousRegne: ISousRegne): Observable<EntityResponseType> {
    return this.http.patch<ISousRegne>(`${this.resourceUrl}/${getSousRegneIdentifier(sousRegne) as number}`, sousRegne, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousRegne>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousRegne[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousRegneToCollectionIfMissing(
    sousRegneCollection: ISousRegne[],
    ...sousRegnesToCheck: (ISousRegne | null | undefined)[]
  ): ISousRegne[] {
    const sousRegnes: ISousRegne[] = sousRegnesToCheck.filter(isPresent);
    if (sousRegnes.length > 0) {
      const sousRegneCollectionIdentifiers = sousRegneCollection.map(sousRegneItem => getSousRegneIdentifier(sousRegneItem)!);
      const sousRegnesToAdd = sousRegnes.filter(sousRegneItem => {
        const sousRegneIdentifier = getSousRegneIdentifier(sousRegneItem);
        if (sousRegneIdentifier == null || sousRegneCollectionIdentifiers.includes(sousRegneIdentifier)) {
          return false;
        }
        sousRegneCollectionIdentifiers.push(sousRegneIdentifier);
        return true;
      });
      return [...sousRegnesToAdd, ...sousRegneCollection];
    }
    return sousRegneCollection;
  }
}
