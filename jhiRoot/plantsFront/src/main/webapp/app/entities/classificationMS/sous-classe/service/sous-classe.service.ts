import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousClasse, getSousClasseIdentifier } from '../sous-classe.model';

export type EntityResponseType = HttpResponse<ISousClasse>;
export type EntityArrayResponseType = HttpResponse<ISousClasse[]>;

@Injectable({ providedIn: 'root' })
export class SousClasseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-classes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousClasse: ISousClasse): Observable<EntityResponseType> {
    return this.http.post<ISousClasse>(this.resourceUrl, sousClasse, { observe: 'response' });
  }

  update(sousClasse: ISousClasse): Observable<EntityResponseType> {
    return this.http.put<ISousClasse>(`${this.resourceUrl}/${getSousClasseIdentifier(sousClasse) as number}`, sousClasse, {
      observe: 'response',
    });
  }

  partialUpdate(sousClasse: ISousClasse): Observable<EntityResponseType> {
    return this.http.patch<ISousClasse>(`${this.resourceUrl}/${getSousClasseIdentifier(sousClasse) as number}`, sousClasse, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousClasse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousClasse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousClasseToCollectionIfMissing(
    sousClasseCollection: ISousClasse[],
    ...sousClassesToCheck: (ISousClasse | null | undefined)[]
  ): ISousClasse[] {
    const sousClasses: ISousClasse[] = sousClassesToCheck.filter(isPresent);
    if (sousClasses.length > 0) {
      const sousClasseCollectionIdentifiers = sousClasseCollection.map(sousClasseItem => getSousClasseIdentifier(sousClasseItem)!);
      const sousClassesToAdd = sousClasses.filter(sousClasseItem => {
        const sousClasseIdentifier = getSousClasseIdentifier(sousClasseItem);
        if (sousClasseIdentifier == null || sousClasseCollectionIdentifiers.includes(sousClasseIdentifier)) {
          return false;
        }
        sousClasseCollectionIdentifiers.push(sousClasseIdentifier);
        return true;
      });
      return [...sousClassesToAdd, ...sousClasseCollection];
    }
    return sousClasseCollection;
  }
}
