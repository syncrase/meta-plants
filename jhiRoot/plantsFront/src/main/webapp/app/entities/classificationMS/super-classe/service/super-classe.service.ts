import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISuperClasse, getSuperClasseIdentifier } from '../super-classe.model';

export type EntityResponseType = HttpResponse<ISuperClasse>;
export type EntityArrayResponseType = HttpResponse<ISuperClasse[]>;

@Injectable({ providedIn: 'root' })
export class SuperClasseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/super-classes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(superClasse: ISuperClasse): Observable<EntityResponseType> {
    return this.http.post<ISuperClasse>(this.resourceUrl, superClasse, { observe: 'response' });
  }

  update(superClasse: ISuperClasse): Observable<EntityResponseType> {
    return this.http.put<ISuperClasse>(`${this.resourceUrl}/${getSuperClasseIdentifier(superClasse) as number}`, superClasse, {
      observe: 'response',
    });
  }

  partialUpdate(superClasse: ISuperClasse): Observable<EntityResponseType> {
    return this.http.patch<ISuperClasse>(`${this.resourceUrl}/${getSuperClasseIdentifier(superClasse) as number}`, superClasse, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISuperClasse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISuperClasse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSuperClasseToCollectionIfMissing(
    superClasseCollection: ISuperClasse[],
    ...superClassesToCheck: (ISuperClasse | null | undefined)[]
  ): ISuperClasse[] {
    const superClasses: ISuperClasse[] = superClassesToCheck.filter(isPresent);
    if (superClasses.length > 0) {
      const superClasseCollectionIdentifiers = superClasseCollection.map(superClasseItem => getSuperClasseIdentifier(superClasseItem)!);
      const superClassesToAdd = superClasses.filter(superClasseItem => {
        const superClasseIdentifier = getSuperClasseIdentifier(superClasseItem);
        if (superClasseIdentifier == null || superClasseCollectionIdentifiers.includes(superClasseIdentifier)) {
          return false;
        }
        superClasseCollectionIdentifiers.push(superClasseIdentifier);
        return true;
      });
      return [...superClassesToAdd, ...superClasseCollection];
    }
    return superClasseCollection;
  }
}
