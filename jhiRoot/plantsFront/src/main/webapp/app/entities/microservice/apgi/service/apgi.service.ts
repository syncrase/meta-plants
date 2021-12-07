import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAPGI, getAPGIIdentifier } from '../apgi.model';

export type EntityResponseType = HttpResponse<IAPGI>;
export type EntityArrayResponseType = HttpResponse<IAPGI[]>;

@Injectable({ providedIn: 'root' })
export class APGIService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apgis', 'plantsms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aPGI: IAPGI): Observable<EntityResponseType> {
    return this.http.post<IAPGI>(this.resourceUrl, aPGI, { observe: 'response' });
  }

  update(aPGI: IAPGI): Observable<EntityResponseType> {
    return this.http.put<IAPGI>(`${this.resourceUrl}/${getAPGIIdentifier(aPGI) as number}`, aPGI, { observe: 'response' });
  }

  partialUpdate(aPGI: IAPGI): Observable<EntityResponseType> {
    return this.http.patch<IAPGI>(`${this.resourceUrl}/${getAPGIIdentifier(aPGI) as number}`, aPGI, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGI>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGI[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAPGIToCollectionIfMissing(aPGICollection: IAPGI[], ...aPGISToCheck: (IAPGI | null | undefined)[]): IAPGI[] {
    const aPGIS: IAPGI[] = aPGISToCheck.filter(isPresent);
    if (aPGIS.length > 0) {
      const aPGICollectionIdentifiers = aPGICollection.map(aPGIItem => getAPGIIdentifier(aPGIItem)!);
      const aPGISToAdd = aPGIS.filter(aPGIItem => {
        const aPGIIdentifier = getAPGIIdentifier(aPGIItem);
        if (aPGIIdentifier == null || aPGICollectionIdentifiers.includes(aPGIIdentifier)) {
          return false;
        }
        aPGICollectionIdentifiers.push(aPGIIdentifier);
        return true;
      });
      return [...aPGISToAdd, ...aPGICollection];
    }
    return aPGICollection;
  }
}
