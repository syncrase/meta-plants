import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITribu, getTribuIdentifier } from '../tribu.model';

export type EntityResponseType = HttpResponse<ITribu>;
export type EntityArrayResponseType = HttpResponse<ITribu[]>;

@Injectable({ providedIn: 'root' })
export class TribuService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tribus', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tribu: ITribu): Observable<EntityResponseType> {
    return this.http.post<ITribu>(this.resourceUrl, tribu, { observe: 'response' });
  }

  update(tribu: ITribu): Observable<EntityResponseType> {
    return this.http.put<ITribu>(`${this.resourceUrl}/${getTribuIdentifier(tribu) as number}`, tribu, { observe: 'response' });
  }

  partialUpdate(tribu: ITribu): Observable<EntityResponseType> {
    return this.http.patch<ITribu>(`${this.resourceUrl}/${getTribuIdentifier(tribu) as number}`, tribu, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITribu>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITribu[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTribuToCollectionIfMissing(tribuCollection: ITribu[], ...tribusToCheck: (ITribu | null | undefined)[]): ITribu[] {
    const tribus: ITribu[] = tribusToCheck.filter(isPresent);
    if (tribus.length > 0) {
      const tribuCollectionIdentifiers = tribuCollection.map(tribuItem => getTribuIdentifier(tribuItem)!);
      const tribusToAdd = tribus.filter(tribuItem => {
        const tribuIdentifier = getTribuIdentifier(tribuItem);
        if (tribuIdentifier == null || tribuCollectionIdentifiers.includes(tribuIdentifier)) {
          return false;
        }
        tribuCollectionIdentifiers.push(tribuIdentifier);
        return true;
      });
      return [...tribusToAdd, ...tribuCollection];
    }
    return tribuCollection;
  }
}
