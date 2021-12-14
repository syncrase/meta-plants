import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousForme, getSousFormeIdentifier } from '../sous-forme.model';

export type EntityResponseType = HttpResponse<ISousForme>;
export type EntityArrayResponseType = HttpResponse<ISousForme[]>;

@Injectable({ providedIn: 'root' })
export class SousFormeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-formes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousForme: ISousForme): Observable<EntityResponseType> {
    return this.http.post<ISousForme>(this.resourceUrl, sousForme, { observe: 'response' });
  }

  update(sousForme: ISousForme): Observable<EntityResponseType> {
    return this.http.put<ISousForme>(`${this.resourceUrl}/${getSousFormeIdentifier(sousForme) as number}`, sousForme, {
      observe: 'response',
    });
  }

  partialUpdate(sousForme: ISousForme): Observable<EntityResponseType> {
    return this.http.patch<ISousForme>(`${this.resourceUrl}/${getSousFormeIdentifier(sousForme) as number}`, sousForme, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousForme>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousForme[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousFormeToCollectionIfMissing(
    sousFormeCollection: ISousForme[],
    ...sousFormesToCheck: (ISousForme | null | undefined)[]
  ): ISousForme[] {
    const sousFormes: ISousForme[] = sousFormesToCheck.filter(isPresent);
    if (sousFormes.length > 0) {
      const sousFormeCollectionIdentifiers = sousFormeCollection.map(sousFormeItem => getSousFormeIdentifier(sousFormeItem)!);
      const sousFormesToAdd = sousFormes.filter(sousFormeItem => {
        const sousFormeIdentifier = getSousFormeIdentifier(sousFormeItem);
        if (sousFormeIdentifier == null || sousFormeCollectionIdentifiers.includes(sousFormeIdentifier)) {
          return false;
        }
        sousFormeCollectionIdentifiers.push(sousFormeIdentifier);
        return true;
      });
      return [...sousFormesToAdd, ...sousFormeCollection];
    }
    return sousFormeCollection;
  }
}
