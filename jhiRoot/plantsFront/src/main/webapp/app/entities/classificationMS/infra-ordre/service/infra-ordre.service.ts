import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInfraOrdre, getInfraOrdreIdentifier } from '../infra-ordre.model';

export type EntityResponseType = HttpResponse<IInfraOrdre>;
export type EntityArrayResponseType = HttpResponse<IInfraOrdre[]>;

@Injectable({ providedIn: 'root' })
export class InfraOrdreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/infra-ordres', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(infraOrdre: IInfraOrdre): Observable<EntityResponseType> {
    return this.http.post<IInfraOrdre>(this.resourceUrl, infraOrdre, { observe: 'response' });
  }

  update(infraOrdre: IInfraOrdre): Observable<EntityResponseType> {
    return this.http.put<IInfraOrdre>(`${this.resourceUrl}/${getInfraOrdreIdentifier(infraOrdre) as number}`, infraOrdre, {
      observe: 'response',
    });
  }

  partialUpdate(infraOrdre: IInfraOrdre): Observable<EntityResponseType> {
    return this.http.patch<IInfraOrdre>(`${this.resourceUrl}/${getInfraOrdreIdentifier(infraOrdre) as number}`, infraOrdre, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInfraOrdre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInfraOrdre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInfraOrdreToCollectionIfMissing(
    infraOrdreCollection: IInfraOrdre[],
    ...infraOrdresToCheck: (IInfraOrdre | null | undefined)[]
  ): IInfraOrdre[] {
    const infraOrdres: IInfraOrdre[] = infraOrdresToCheck.filter(isPresent);
    if (infraOrdres.length > 0) {
      const infraOrdreCollectionIdentifiers = infraOrdreCollection.map(infraOrdreItem => getInfraOrdreIdentifier(infraOrdreItem)!);
      const infraOrdresToAdd = infraOrdres.filter(infraOrdreItem => {
        const infraOrdreIdentifier = getInfraOrdreIdentifier(infraOrdreItem);
        if (infraOrdreIdentifier == null || infraOrdreCollectionIdentifiers.includes(infraOrdreIdentifier)) {
          return false;
        }
        infraOrdreCollectionIdentifiers.push(infraOrdreIdentifier);
        return true;
      });
      return [...infraOrdresToAdd, ...infraOrdreCollection];
    }
    return infraOrdreCollection;
  }
}
