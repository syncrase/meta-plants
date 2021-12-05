import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAPGIII, getAPGIIIIdentifier } from '../apgiii.model';

export type EntityResponseType = HttpResponse<IAPGIII>;
export type EntityArrayResponseType = HttpResponse<IAPGIII[]>;

@Injectable({ providedIn: 'root' })
export class APGIIIService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apgiiis', 'microservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aPGIII: IAPGIII): Observable<EntityResponseType> {
    return this.http.post<IAPGIII>(this.resourceUrl, aPGIII, { observe: 'response' });
  }

  update(aPGIII: IAPGIII): Observable<EntityResponseType> {
    return this.http.put<IAPGIII>(`${this.resourceUrl}/${getAPGIIIIdentifier(aPGIII) as number}`, aPGIII, { observe: 'response' });
  }

  partialUpdate(aPGIII: IAPGIII): Observable<EntityResponseType> {
    return this.http.patch<IAPGIII>(`${this.resourceUrl}/${getAPGIIIIdentifier(aPGIII) as number}`, aPGIII, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGIII>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGIII[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAPGIIIToCollectionIfMissing(aPGIIICollection: IAPGIII[], ...aPGIIISToCheck: (IAPGIII | null | undefined)[]): IAPGIII[] {
    const aPGIIIS: IAPGIII[] = aPGIIISToCheck.filter(isPresent);
    if (aPGIIIS.length > 0) {
      const aPGIIICollectionIdentifiers = aPGIIICollection.map(aPGIIIItem => getAPGIIIIdentifier(aPGIIIItem)!);
      const aPGIIISToAdd = aPGIIIS.filter(aPGIIIItem => {
        const aPGIIIIdentifier = getAPGIIIIdentifier(aPGIIIItem);
        if (aPGIIIIdentifier == null || aPGIIICollectionIdentifiers.includes(aPGIIIIdentifier)) {
          return false;
        }
        aPGIIICollectionIdentifiers.push(aPGIIIIdentifier);
        return true;
      });
      return [...aPGIIISToAdd, ...aPGIIICollection];
    }
    return aPGIIICollection;
  }
}
