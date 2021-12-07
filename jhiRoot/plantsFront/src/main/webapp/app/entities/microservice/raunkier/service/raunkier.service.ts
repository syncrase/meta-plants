import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRaunkier, getRaunkierIdentifier } from '../raunkier.model';

export type EntityResponseType = HttpResponse<IRaunkier>;
export type EntityArrayResponseType = HttpResponse<IRaunkier[]>;

@Injectable({ providedIn: 'root' })
export class RaunkierService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/raunkiers', 'plantsms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(raunkier: IRaunkier): Observable<EntityResponseType> {
    return this.http.post<IRaunkier>(this.resourceUrl, raunkier, { observe: 'response' });
  }

  update(raunkier: IRaunkier): Observable<EntityResponseType> {
    return this.http.put<IRaunkier>(`${this.resourceUrl}/${getRaunkierIdentifier(raunkier) as number}`, raunkier, { observe: 'response' });
  }

  partialUpdate(raunkier: IRaunkier): Observable<EntityResponseType> {
    return this.http.patch<IRaunkier>(`${this.resourceUrl}/${getRaunkierIdentifier(raunkier) as number}`, raunkier, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRaunkier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRaunkier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRaunkierToCollectionIfMissing(raunkierCollection: IRaunkier[], ...raunkiersToCheck: (IRaunkier | null | undefined)[]): IRaunkier[] {
    const raunkiers: IRaunkier[] = raunkiersToCheck.filter(isPresent);
    if (raunkiers.length > 0) {
      const raunkierCollectionIdentifiers = raunkierCollection.map(raunkierItem => getRaunkierIdentifier(raunkierItem)!);
      const raunkiersToAdd = raunkiers.filter(raunkierItem => {
        const raunkierIdentifier = getRaunkierIdentifier(raunkierItem);
        if (raunkierIdentifier == null || raunkierCollectionIdentifiers.includes(raunkierIdentifier)) {
          return false;
        }
        raunkierCollectionIdentifiers.push(raunkierIdentifier);
        return true;
      });
      return [...raunkiersToAdd, ...raunkierCollection];
    }
    return raunkierCollection;
  }
}
