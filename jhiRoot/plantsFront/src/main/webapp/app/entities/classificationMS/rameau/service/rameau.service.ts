import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRameau, getRameauIdentifier } from '../rameau.model';

export type EntityResponseType = HttpResponse<IRameau>;
export type EntityArrayResponseType = HttpResponse<IRameau[]>;

@Injectable({ providedIn: 'root' })
export class RameauService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rameaus', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rameau: IRameau): Observable<EntityResponseType> {
    return this.http.post<IRameau>(this.resourceUrl, rameau, { observe: 'response' });
  }

  update(rameau: IRameau): Observable<EntityResponseType> {
    return this.http.put<IRameau>(`${this.resourceUrl}/${getRameauIdentifier(rameau) as number}`, rameau, { observe: 'response' });
  }

  partialUpdate(rameau: IRameau): Observable<EntityResponseType> {
    return this.http.patch<IRameau>(`${this.resourceUrl}/${getRameauIdentifier(rameau) as number}`, rameau, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRameau>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRameau[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRameauToCollectionIfMissing(rameauCollection: IRameau[], ...rameausToCheck: (IRameau | null | undefined)[]): IRameau[] {
    const rameaus: IRameau[] = rameausToCheck.filter(isPresent);
    if (rameaus.length > 0) {
      const rameauCollectionIdentifiers = rameauCollection.map(rameauItem => getRameauIdentifier(rameauItem)!);
      const rameausToAdd = rameaus.filter(rameauItem => {
        const rameauIdentifier = getRameauIdentifier(rameauItem);
        if (rameauIdentifier == null || rameauCollectionIdentifiers.includes(rameauIdentifier)) {
          return false;
        }
        rameauCollectionIdentifiers.push(rameauIdentifier);
        return true;
      });
      return [...rameausToAdd, ...rameauCollection];
    }
    return rameauCollection;
  }
}
