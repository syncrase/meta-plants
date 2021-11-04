import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAPGII, getAPGIIIdentifier } from '../apgii.model';

export type EntityResponseType = HttpResponse<IAPGII>;
export type EntityArrayResponseType = HttpResponse<IAPGII[]>;

@Injectable({ providedIn: 'root' })
export class APGIIService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apgiis', 'microservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aPGII: IAPGII): Observable<EntityResponseType> {
    return this.http.post<IAPGII>(this.resourceUrl, aPGII, { observe: 'response' });
  }

  update(aPGII: IAPGII): Observable<EntityResponseType> {
    return this.http.put<IAPGII>(`${this.resourceUrl}/${getAPGIIIdentifier(aPGII) as number}`, aPGII, { observe: 'response' });
  }

  partialUpdate(aPGII: IAPGII): Observable<EntityResponseType> {
    return this.http.patch<IAPGII>(`${this.resourceUrl}/${getAPGIIIdentifier(aPGII) as number}`, aPGII, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGII>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGII[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAPGIIToCollectionIfMissing(aPGIICollection: IAPGII[], ...aPGIISToCheck: (IAPGII | null | undefined)[]): IAPGII[] {
    const aPGIIS: IAPGII[] = aPGIISToCheck.filter(isPresent);
    if (aPGIIS.length > 0) {
      const aPGIICollectionIdentifiers = aPGIICollection.map(aPGIIItem => getAPGIIIdentifier(aPGIIItem)!);
      const aPGIISToAdd = aPGIIS.filter(aPGIIItem => {
        const aPGIIIdentifier = getAPGIIIdentifier(aPGIIItem);
        if (aPGIIIdentifier == null || aPGIICollectionIdentifiers.includes(aPGIIIdentifier)) {
          return false;
        }
        aPGIICollectionIdentifiers.push(aPGIIIdentifier);
        return true;
      });
      return [...aPGIISToAdd, ...aPGIICollection];
    }
    return aPGIICollection;
  }
}
