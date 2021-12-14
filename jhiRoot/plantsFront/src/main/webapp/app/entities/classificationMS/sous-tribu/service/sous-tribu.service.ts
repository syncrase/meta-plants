import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousTribu, getSousTribuIdentifier } from '../sous-tribu.model';

export type EntityResponseType = HttpResponse<ISousTribu>;
export type EntityArrayResponseType = HttpResponse<ISousTribu[]>;

@Injectable({ providedIn: 'root' })
export class SousTribuService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-tribus', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousTribu: ISousTribu): Observable<EntityResponseType> {
    return this.http.post<ISousTribu>(this.resourceUrl, sousTribu, { observe: 'response' });
  }

  update(sousTribu: ISousTribu): Observable<EntityResponseType> {
    return this.http.put<ISousTribu>(`${this.resourceUrl}/${getSousTribuIdentifier(sousTribu) as number}`, sousTribu, {
      observe: 'response',
    });
  }

  partialUpdate(sousTribu: ISousTribu): Observable<EntityResponseType> {
    return this.http.patch<ISousTribu>(`${this.resourceUrl}/${getSousTribuIdentifier(sousTribu) as number}`, sousTribu, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousTribu>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousTribu[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousTribuToCollectionIfMissing(
    sousTribuCollection: ISousTribu[],
    ...sousTribusToCheck: (ISousTribu | null | undefined)[]
  ): ISousTribu[] {
    const sousTribus: ISousTribu[] = sousTribusToCheck.filter(isPresent);
    if (sousTribus.length > 0) {
      const sousTribuCollectionIdentifiers = sousTribuCollection.map(sousTribuItem => getSousTribuIdentifier(sousTribuItem)!);
      const sousTribusToAdd = sousTribus.filter(sousTribuItem => {
        const sousTribuIdentifier = getSousTribuIdentifier(sousTribuItem);
        if (sousTribuIdentifier == null || sousTribuCollectionIdentifiers.includes(sousTribuIdentifier)) {
          return false;
        }
        sousTribuCollectionIdentifiers.push(sousTribuIdentifier);
        return true;
      });
      return [...sousTribusToAdd, ...sousTribuCollection];
    }
    return sousTribuCollection;
  }
}
