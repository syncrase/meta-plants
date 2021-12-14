import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousVariete, getSousVarieteIdentifier } from '../sous-variete.model';

export type EntityResponseType = HttpResponse<ISousVariete>;
export type EntityArrayResponseType = HttpResponse<ISousVariete[]>;

@Injectable({ providedIn: 'root' })
export class SousVarieteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-varietes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousVariete: ISousVariete): Observable<EntityResponseType> {
    return this.http.post<ISousVariete>(this.resourceUrl, sousVariete, { observe: 'response' });
  }

  update(sousVariete: ISousVariete): Observable<EntityResponseType> {
    return this.http.put<ISousVariete>(`${this.resourceUrl}/${getSousVarieteIdentifier(sousVariete) as number}`, sousVariete, {
      observe: 'response',
    });
  }

  partialUpdate(sousVariete: ISousVariete): Observable<EntityResponseType> {
    return this.http.patch<ISousVariete>(`${this.resourceUrl}/${getSousVarieteIdentifier(sousVariete) as number}`, sousVariete, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousVariete>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousVariete[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousVarieteToCollectionIfMissing(
    sousVarieteCollection: ISousVariete[],
    ...sousVarietesToCheck: (ISousVariete | null | undefined)[]
  ): ISousVariete[] {
    const sousVarietes: ISousVariete[] = sousVarietesToCheck.filter(isPresent);
    if (sousVarietes.length > 0) {
      const sousVarieteCollectionIdentifiers = sousVarieteCollection.map(sousVarieteItem => getSousVarieteIdentifier(sousVarieteItem)!);
      const sousVarietesToAdd = sousVarietes.filter(sousVarieteItem => {
        const sousVarieteIdentifier = getSousVarieteIdentifier(sousVarieteItem);
        if (sousVarieteIdentifier == null || sousVarieteCollectionIdentifiers.includes(sousVarieteIdentifier)) {
          return false;
        }
        sousVarieteCollectionIdentifiers.push(sousVarieteIdentifier);
        return true;
      });
      return [...sousVarietesToAdd, ...sousVarieteCollection];
    }
    return sousVarieteCollection;
  }
}
