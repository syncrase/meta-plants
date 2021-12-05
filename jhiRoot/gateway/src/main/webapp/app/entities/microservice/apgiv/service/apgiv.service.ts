import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAPGIV, getAPGIVIdentifier } from '../apgiv.model';

export type EntityResponseType = HttpResponse<IAPGIV>;
export type EntityArrayResponseType = HttpResponse<IAPGIV[]>;

@Injectable({ providedIn: 'root' })
export class APGIVService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apgivs', 'microservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aPGIV: IAPGIV): Observable<EntityResponseType> {
    return this.http.post<IAPGIV>(this.resourceUrl, aPGIV, { observe: 'response' });
  }

  update(aPGIV: IAPGIV): Observable<EntityResponseType> {
    return this.http.put<IAPGIV>(`${this.resourceUrl}/${getAPGIVIdentifier(aPGIV) as number}`, aPGIV, { observe: 'response' });
  }

  partialUpdate(aPGIV: IAPGIV): Observable<EntityResponseType> {
    return this.http.patch<IAPGIV>(`${this.resourceUrl}/${getAPGIVIdentifier(aPGIV) as number}`, aPGIV, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGIV>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGIV[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAPGIVToCollectionIfMissing(aPGIVCollection: IAPGIV[], ...aPGIVSToCheck: (IAPGIV | null | undefined)[]): IAPGIV[] {
    const aPGIVS: IAPGIV[] = aPGIVSToCheck.filter(isPresent);
    if (aPGIVS.length > 0) {
      const aPGIVCollectionIdentifiers = aPGIVCollection.map(aPGIVItem => getAPGIVIdentifier(aPGIVItem)!);
      const aPGIVSToAdd = aPGIVS.filter(aPGIVItem => {
        const aPGIVIdentifier = getAPGIVIdentifier(aPGIVItem);
        if (aPGIVIdentifier == null || aPGIVCollectionIdentifiers.includes(aPGIVIdentifier)) {
          return false;
        }
        aPGIVCollectionIdentifiers.push(aPGIVIdentifier);
        return true;
      });
      return [...aPGIVSToAdd, ...aPGIVCollection];
    }
    return aPGIVCollection;
  }
}
