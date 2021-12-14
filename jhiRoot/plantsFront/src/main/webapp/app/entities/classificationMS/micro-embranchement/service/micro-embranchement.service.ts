import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMicroEmbranchement, getMicroEmbranchementIdentifier } from '../micro-embranchement.model';

export type EntityResponseType = HttpResponse<IMicroEmbranchement>;
export type EntityArrayResponseType = HttpResponse<IMicroEmbranchement[]>;

@Injectable({ providedIn: 'root' })
export class MicroEmbranchementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/micro-embranchements', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(microEmbranchement: IMicroEmbranchement): Observable<EntityResponseType> {
    return this.http.post<IMicroEmbranchement>(this.resourceUrl, microEmbranchement, { observe: 'response' });
  }

  update(microEmbranchement: IMicroEmbranchement): Observable<EntityResponseType> {
    return this.http.put<IMicroEmbranchement>(
      `${this.resourceUrl}/${getMicroEmbranchementIdentifier(microEmbranchement) as number}`,
      microEmbranchement,
      { observe: 'response' }
    );
  }

  partialUpdate(microEmbranchement: IMicroEmbranchement): Observable<EntityResponseType> {
    return this.http.patch<IMicroEmbranchement>(
      `${this.resourceUrl}/${getMicroEmbranchementIdentifier(microEmbranchement) as number}`,
      microEmbranchement,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMicroEmbranchement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMicroEmbranchement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMicroEmbranchementToCollectionIfMissing(
    microEmbranchementCollection: IMicroEmbranchement[],
    ...microEmbranchementsToCheck: (IMicroEmbranchement | null | undefined)[]
  ): IMicroEmbranchement[] {
    const microEmbranchements: IMicroEmbranchement[] = microEmbranchementsToCheck.filter(isPresent);
    if (microEmbranchements.length > 0) {
      const microEmbranchementCollectionIdentifiers = microEmbranchementCollection.map(
        microEmbranchementItem => getMicroEmbranchementIdentifier(microEmbranchementItem)!
      );
      const microEmbranchementsToAdd = microEmbranchements.filter(microEmbranchementItem => {
        const microEmbranchementIdentifier = getMicroEmbranchementIdentifier(microEmbranchementItem);
        if (microEmbranchementIdentifier == null || microEmbranchementCollectionIdentifiers.includes(microEmbranchementIdentifier)) {
          return false;
        }
        microEmbranchementCollectionIdentifiers.push(microEmbranchementIdentifier);
        return true;
      });
      return [...microEmbranchementsToAdd, ...microEmbranchementCollection];
    }
    return microEmbranchementCollection;
  }
}
