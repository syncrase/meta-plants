import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInfraRegne, getInfraRegneIdentifier } from '../infra-regne.model';

export type EntityResponseType = HttpResponse<IInfraRegne>;
export type EntityArrayResponseType = HttpResponse<IInfraRegne[]>;

@Injectable({ providedIn: 'root' })
export class InfraRegneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/infra-regnes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(infraRegne: IInfraRegne): Observable<EntityResponseType> {
    return this.http.post<IInfraRegne>(this.resourceUrl, infraRegne, { observe: 'response' });
  }

  update(infraRegne: IInfraRegne): Observable<EntityResponseType> {
    return this.http.put<IInfraRegne>(`${this.resourceUrl}/${getInfraRegneIdentifier(infraRegne) as number}`, infraRegne, {
      observe: 'response',
    });
  }

  partialUpdate(infraRegne: IInfraRegne): Observable<EntityResponseType> {
    return this.http.patch<IInfraRegne>(`${this.resourceUrl}/${getInfraRegneIdentifier(infraRegne) as number}`, infraRegne, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInfraRegne>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInfraRegne[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInfraRegneToCollectionIfMissing(
    infraRegneCollection: IInfraRegne[],
    ...infraRegnesToCheck: (IInfraRegne | null | undefined)[]
  ): IInfraRegne[] {
    const infraRegnes: IInfraRegne[] = infraRegnesToCheck.filter(isPresent);
    if (infraRegnes.length > 0) {
      const infraRegneCollectionIdentifiers = infraRegneCollection.map(infraRegneItem => getInfraRegneIdentifier(infraRegneItem)!);
      const infraRegnesToAdd = infraRegnes.filter(infraRegneItem => {
        const infraRegneIdentifier = getInfraRegneIdentifier(infraRegneItem);
        if (infraRegneIdentifier == null || infraRegneCollectionIdentifiers.includes(infraRegneIdentifier)) {
          return false;
        }
        infraRegneCollectionIdentifiers.push(infraRegneIdentifier);
        return true;
      });
      return [...infraRegnesToAdd, ...infraRegneCollection];
    }
    return infraRegneCollection;
  }
}
