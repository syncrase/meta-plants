import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousDivision, getSousDivisionIdentifier } from '../sous-division.model';

export type EntityResponseType = HttpResponse<ISousDivision>;
export type EntityArrayResponseType = HttpResponse<ISousDivision[]>;

@Injectable({ providedIn: 'root' })
export class SousDivisionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-divisions', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousDivision: ISousDivision): Observable<EntityResponseType> {
    return this.http.post<ISousDivision>(this.resourceUrl, sousDivision, { observe: 'response' });
  }

  update(sousDivision: ISousDivision): Observable<EntityResponseType> {
    return this.http.put<ISousDivision>(`${this.resourceUrl}/${getSousDivisionIdentifier(sousDivision) as number}`, sousDivision, {
      observe: 'response',
    });
  }

  partialUpdate(sousDivision: ISousDivision): Observable<EntityResponseType> {
    return this.http.patch<ISousDivision>(`${this.resourceUrl}/${getSousDivisionIdentifier(sousDivision) as number}`, sousDivision, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousDivision>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousDivision[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousDivisionToCollectionIfMissing(
    sousDivisionCollection: ISousDivision[],
    ...sousDivisionsToCheck: (ISousDivision | null | undefined)[]
  ): ISousDivision[] {
    const sousDivisions: ISousDivision[] = sousDivisionsToCheck.filter(isPresent);
    if (sousDivisions.length > 0) {
      const sousDivisionCollectionIdentifiers = sousDivisionCollection.map(
        sousDivisionItem => getSousDivisionIdentifier(sousDivisionItem)!
      );
      const sousDivisionsToAdd = sousDivisions.filter(sousDivisionItem => {
        const sousDivisionIdentifier = getSousDivisionIdentifier(sousDivisionItem);
        if (sousDivisionIdentifier == null || sousDivisionCollectionIdentifiers.includes(sousDivisionIdentifier)) {
          return false;
        }
        sousDivisionCollectionIdentifiers.push(sousDivisionIdentifier);
        return true;
      });
      return [...sousDivisionsToAdd, ...sousDivisionCollection];
    }
    return sousDivisionCollection;
  }
}
