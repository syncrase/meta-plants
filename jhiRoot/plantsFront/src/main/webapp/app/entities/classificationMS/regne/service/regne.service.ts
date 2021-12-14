import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRegne, getRegneIdentifier } from '../regne.model';

export type EntityResponseType = HttpResponse<IRegne>;
export type EntityArrayResponseType = HttpResponse<IRegne[]>;

@Injectable({ providedIn: 'root' })
export class RegneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/regnes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(regne: IRegne): Observable<EntityResponseType> {
    return this.http.post<IRegne>(this.resourceUrl, regne, { observe: 'response' });
  }

  update(regne: IRegne): Observable<EntityResponseType> {
    return this.http.put<IRegne>(`${this.resourceUrl}/${getRegneIdentifier(regne) as number}`, regne, { observe: 'response' });
  }

  partialUpdate(regne: IRegne): Observable<EntityResponseType> {
    return this.http.patch<IRegne>(`${this.resourceUrl}/${getRegneIdentifier(regne) as number}`, regne, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRegne>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRegne[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRegneToCollectionIfMissing(regneCollection: IRegne[], ...regnesToCheck: (IRegne | null | undefined)[]): IRegne[] {
    const regnes: IRegne[] = regnesToCheck.filter(isPresent);
    if (regnes.length > 0) {
      const regneCollectionIdentifiers = regneCollection.map(regneItem => getRegneIdentifier(regneItem)!);
      const regnesToAdd = regnes.filter(regneItem => {
        const regneIdentifier = getRegneIdentifier(regneItem);
        if (regneIdentifier == null || regneCollectionIdentifiers.includes(regneIdentifier)) {
          return false;
        }
        regneCollectionIdentifiers.push(regneIdentifier);
        return true;
      });
      return [...regnesToAdd, ...regneCollection];
    }
    return regneCollection;
  }
}
