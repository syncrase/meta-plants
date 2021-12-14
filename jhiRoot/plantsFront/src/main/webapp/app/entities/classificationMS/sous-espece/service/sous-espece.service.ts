import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousEspece, getSousEspeceIdentifier } from '../sous-espece.model';

export type EntityResponseType = HttpResponse<ISousEspece>;
export type EntityArrayResponseType = HttpResponse<ISousEspece[]>;

@Injectable({ providedIn: 'root' })
export class SousEspeceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-especes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousEspece: ISousEspece): Observable<EntityResponseType> {
    return this.http.post<ISousEspece>(this.resourceUrl, sousEspece, { observe: 'response' });
  }

  update(sousEspece: ISousEspece): Observable<EntityResponseType> {
    return this.http.put<ISousEspece>(`${this.resourceUrl}/${getSousEspeceIdentifier(sousEspece) as number}`, sousEspece, {
      observe: 'response',
    });
  }

  partialUpdate(sousEspece: ISousEspece): Observable<EntityResponseType> {
    return this.http.patch<ISousEspece>(`${this.resourceUrl}/${getSousEspeceIdentifier(sousEspece) as number}`, sousEspece, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousEspece>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousEspece[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousEspeceToCollectionIfMissing(
    sousEspeceCollection: ISousEspece[],
    ...sousEspecesToCheck: (ISousEspece | null | undefined)[]
  ): ISousEspece[] {
    const sousEspeces: ISousEspece[] = sousEspecesToCheck.filter(isPresent);
    if (sousEspeces.length > 0) {
      const sousEspeceCollectionIdentifiers = sousEspeceCollection.map(sousEspeceItem => getSousEspeceIdentifier(sousEspeceItem)!);
      const sousEspecesToAdd = sousEspeces.filter(sousEspeceItem => {
        const sousEspeceIdentifier = getSousEspeceIdentifier(sousEspeceItem);
        if (sousEspeceIdentifier == null || sousEspeceCollectionIdentifiers.includes(sousEspeceIdentifier)) {
          return false;
        }
        sousEspeceCollectionIdentifiers.push(sousEspeceIdentifier);
        return true;
      });
      return [...sousEspecesToAdd, ...sousEspeceCollection];
    }
    return sousEspeceCollection;
  }
}
