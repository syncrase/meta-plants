import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISuperFamille, getSuperFamilleIdentifier } from '../super-famille.model';

export type EntityResponseType = HttpResponse<ISuperFamille>;
export type EntityArrayResponseType = HttpResponse<ISuperFamille[]>;

@Injectable({ providedIn: 'root' })
export class SuperFamilleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/super-familles', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(superFamille: ISuperFamille): Observable<EntityResponseType> {
    return this.http.post<ISuperFamille>(this.resourceUrl, superFamille, { observe: 'response' });
  }

  update(superFamille: ISuperFamille): Observable<EntityResponseType> {
    return this.http.put<ISuperFamille>(`${this.resourceUrl}/${getSuperFamilleIdentifier(superFamille) as number}`, superFamille, {
      observe: 'response',
    });
  }

  partialUpdate(superFamille: ISuperFamille): Observable<EntityResponseType> {
    return this.http.patch<ISuperFamille>(`${this.resourceUrl}/${getSuperFamilleIdentifier(superFamille) as number}`, superFamille, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISuperFamille>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISuperFamille[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSuperFamilleToCollectionIfMissing(
    superFamilleCollection: ISuperFamille[],
    ...superFamillesToCheck: (ISuperFamille | null | undefined)[]
  ): ISuperFamille[] {
    const superFamilles: ISuperFamille[] = superFamillesToCheck.filter(isPresent);
    if (superFamilles.length > 0) {
      const superFamilleCollectionIdentifiers = superFamilleCollection.map(
        superFamilleItem => getSuperFamilleIdentifier(superFamilleItem)!
      );
      const superFamillesToAdd = superFamilles.filter(superFamilleItem => {
        const superFamilleIdentifier = getSuperFamilleIdentifier(superFamilleItem);
        if (superFamilleIdentifier == null || superFamilleCollectionIdentifiers.includes(superFamilleIdentifier)) {
          return false;
        }
        superFamilleCollectionIdentifiers.push(superFamilleIdentifier);
        return true;
      });
      return [...superFamillesToAdd, ...superFamilleCollection];
    }
    return superFamilleCollection;
  }
}
