import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISuperDivision, getSuperDivisionIdentifier } from '../super-division.model';

export type EntityResponseType = HttpResponse<ISuperDivision>;
export type EntityArrayResponseType = HttpResponse<ISuperDivision[]>;

@Injectable({ providedIn: 'root' })
export class SuperDivisionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/super-divisions', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(superDivision: ISuperDivision): Observable<EntityResponseType> {
    return this.http.post<ISuperDivision>(this.resourceUrl, superDivision, { observe: 'response' });
  }

  update(superDivision: ISuperDivision): Observable<EntityResponseType> {
    return this.http.put<ISuperDivision>(`${this.resourceUrl}/${getSuperDivisionIdentifier(superDivision) as number}`, superDivision, {
      observe: 'response',
    });
  }

  partialUpdate(superDivision: ISuperDivision): Observable<EntityResponseType> {
    return this.http.patch<ISuperDivision>(`${this.resourceUrl}/${getSuperDivisionIdentifier(superDivision) as number}`, superDivision, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISuperDivision>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISuperDivision[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSuperDivisionToCollectionIfMissing(
    superDivisionCollection: ISuperDivision[],
    ...superDivisionsToCheck: (ISuperDivision | null | undefined)[]
  ): ISuperDivision[] {
    const superDivisions: ISuperDivision[] = superDivisionsToCheck.filter(isPresent);
    if (superDivisions.length > 0) {
      const superDivisionCollectionIdentifiers = superDivisionCollection.map(
        superDivisionItem => getSuperDivisionIdentifier(superDivisionItem)!
      );
      const superDivisionsToAdd = superDivisions.filter(superDivisionItem => {
        const superDivisionIdentifier = getSuperDivisionIdentifier(superDivisionItem);
        if (superDivisionIdentifier == null || superDivisionCollectionIdentifiers.includes(superDivisionIdentifier)) {
          return false;
        }
        superDivisionCollectionIdentifiers.push(superDivisionIdentifier);
        return true;
      });
      return [...superDivisionsToAdd, ...superDivisionCollection];
    }
    return superDivisionCollection;
  }
}
