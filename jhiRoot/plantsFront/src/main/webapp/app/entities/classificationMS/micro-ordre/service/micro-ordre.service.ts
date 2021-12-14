import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMicroOrdre, getMicroOrdreIdentifier } from '../micro-ordre.model';

export type EntityResponseType = HttpResponse<IMicroOrdre>;
export type EntityArrayResponseType = HttpResponse<IMicroOrdre[]>;

@Injectable({ providedIn: 'root' })
export class MicroOrdreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/micro-ordres', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(microOrdre: IMicroOrdre): Observable<EntityResponseType> {
    return this.http.post<IMicroOrdre>(this.resourceUrl, microOrdre, { observe: 'response' });
  }

  update(microOrdre: IMicroOrdre): Observable<EntityResponseType> {
    return this.http.put<IMicroOrdre>(`${this.resourceUrl}/${getMicroOrdreIdentifier(microOrdre) as number}`, microOrdre, {
      observe: 'response',
    });
  }

  partialUpdate(microOrdre: IMicroOrdre): Observable<EntityResponseType> {
    return this.http.patch<IMicroOrdre>(`${this.resourceUrl}/${getMicroOrdreIdentifier(microOrdre) as number}`, microOrdre, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMicroOrdre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMicroOrdre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMicroOrdreToCollectionIfMissing(
    microOrdreCollection: IMicroOrdre[],
    ...microOrdresToCheck: (IMicroOrdre | null | undefined)[]
  ): IMicroOrdre[] {
    const microOrdres: IMicroOrdre[] = microOrdresToCheck.filter(isPresent);
    if (microOrdres.length > 0) {
      const microOrdreCollectionIdentifiers = microOrdreCollection.map(microOrdreItem => getMicroOrdreIdentifier(microOrdreItem)!);
      const microOrdresToAdd = microOrdres.filter(microOrdreItem => {
        const microOrdreIdentifier = getMicroOrdreIdentifier(microOrdreItem);
        if (microOrdreIdentifier == null || microOrdreCollectionIdentifiers.includes(microOrdreIdentifier)) {
          return false;
        }
        microOrdreCollectionIdentifiers.push(microOrdreIdentifier);
        return true;
      });
      return [...microOrdresToAdd, ...microOrdreCollection];
    }
    return microOrdreCollection;
  }
}
