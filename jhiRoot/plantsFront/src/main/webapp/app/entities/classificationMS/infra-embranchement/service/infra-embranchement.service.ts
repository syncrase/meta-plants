import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInfraEmbranchement, getInfraEmbranchementIdentifier } from '../infra-embranchement.model';

export type EntityResponseType = HttpResponse<IInfraEmbranchement>;
export type EntityArrayResponseType = HttpResponse<IInfraEmbranchement[]>;

@Injectable({ providedIn: 'root' })
export class InfraEmbranchementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/infra-embranchements', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(infraEmbranchement: IInfraEmbranchement): Observable<EntityResponseType> {
    return this.http.post<IInfraEmbranchement>(this.resourceUrl, infraEmbranchement, { observe: 'response' });
  }

  update(infraEmbranchement: IInfraEmbranchement): Observable<EntityResponseType> {
    return this.http.put<IInfraEmbranchement>(
      `${this.resourceUrl}/${getInfraEmbranchementIdentifier(infraEmbranchement) as number}`,
      infraEmbranchement,
      { observe: 'response' }
    );
  }

  partialUpdate(infraEmbranchement: IInfraEmbranchement): Observable<EntityResponseType> {
    return this.http.patch<IInfraEmbranchement>(
      `${this.resourceUrl}/${getInfraEmbranchementIdentifier(infraEmbranchement) as number}`,
      infraEmbranchement,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInfraEmbranchement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInfraEmbranchement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInfraEmbranchementToCollectionIfMissing(
    infraEmbranchementCollection: IInfraEmbranchement[],
    ...infraEmbranchementsToCheck: (IInfraEmbranchement | null | undefined)[]
  ): IInfraEmbranchement[] {
    const infraEmbranchements: IInfraEmbranchement[] = infraEmbranchementsToCheck.filter(isPresent);
    if (infraEmbranchements.length > 0) {
      const infraEmbranchementCollectionIdentifiers = infraEmbranchementCollection.map(
        infraEmbranchementItem => getInfraEmbranchementIdentifier(infraEmbranchementItem)!
      );
      const infraEmbranchementsToAdd = infraEmbranchements.filter(infraEmbranchementItem => {
        const infraEmbranchementIdentifier = getInfraEmbranchementIdentifier(infraEmbranchementItem);
        if (infraEmbranchementIdentifier == null || infraEmbranchementCollectionIdentifiers.includes(infraEmbranchementIdentifier)) {
          return false;
        }
        infraEmbranchementCollectionIdentifiers.push(infraEmbranchementIdentifier);
        return true;
      });
      return [...infraEmbranchementsToAdd, ...infraEmbranchementCollection];
    }
    return infraEmbranchementCollection;
  }
}
