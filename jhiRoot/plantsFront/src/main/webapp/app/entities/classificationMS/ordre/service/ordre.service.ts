import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrdre, getOrdreIdentifier } from '../ordre.model';

export type EntityResponseType = HttpResponse<IOrdre>;
export type EntityArrayResponseType = HttpResponse<IOrdre[]>;

@Injectable({ providedIn: 'root' })
export class OrdreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ordres', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ordre: IOrdre): Observable<EntityResponseType> {
    return this.http.post<IOrdre>(this.resourceUrl, ordre, { observe: 'response' });
  }

  update(ordre: IOrdre): Observable<EntityResponseType> {
    return this.http.put<IOrdre>(`${this.resourceUrl}/${getOrdreIdentifier(ordre) as number}`, ordre, { observe: 'response' });
  }

  partialUpdate(ordre: IOrdre): Observable<EntityResponseType> {
    return this.http.patch<IOrdre>(`${this.resourceUrl}/${getOrdreIdentifier(ordre) as number}`, ordre, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrdre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrdre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrdreToCollectionIfMissing(ordreCollection: IOrdre[], ...ordresToCheck: (IOrdre | null | undefined)[]): IOrdre[] {
    const ordres: IOrdre[] = ordresToCheck.filter(isPresent);
    if (ordres.length > 0) {
      const ordreCollectionIdentifiers = ordreCollection.map(ordreItem => getOrdreIdentifier(ordreItem)!);
      const ordresToAdd = ordres.filter(ordreItem => {
        const ordreIdentifier = getOrdreIdentifier(ordreItem);
        if (ordreIdentifier == null || ordreCollectionIdentifiers.includes(ordreIdentifier)) {
          return false;
        }
        ordreCollectionIdentifiers.push(ordreIdentifier);
        return true;
      });
      return [...ordresToAdd, ...ordreCollection];
    }
    return ordreCollection;
  }
}
