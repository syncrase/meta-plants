import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInfraClasse, getInfraClasseIdentifier } from '../infra-classe.model';

export type EntityResponseType = HttpResponse<IInfraClasse>;
export type EntityArrayResponseType = HttpResponse<IInfraClasse[]>;

@Injectable({ providedIn: 'root' })
export class InfraClasseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/infra-classes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(infraClasse: IInfraClasse): Observable<EntityResponseType> {
    return this.http.post<IInfraClasse>(this.resourceUrl, infraClasse, { observe: 'response' });
  }

  update(infraClasse: IInfraClasse): Observable<EntityResponseType> {
    return this.http.put<IInfraClasse>(`${this.resourceUrl}/${getInfraClasseIdentifier(infraClasse) as number}`, infraClasse, {
      observe: 'response',
    });
  }

  partialUpdate(infraClasse: IInfraClasse): Observable<EntityResponseType> {
    return this.http.patch<IInfraClasse>(`${this.resourceUrl}/${getInfraClasseIdentifier(infraClasse) as number}`, infraClasse, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInfraClasse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInfraClasse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInfraClasseToCollectionIfMissing(
    infraClasseCollection: IInfraClasse[],
    ...infraClassesToCheck: (IInfraClasse | null | undefined)[]
  ): IInfraClasse[] {
    const infraClasses: IInfraClasse[] = infraClassesToCheck.filter(isPresent);
    if (infraClasses.length > 0) {
      const infraClasseCollectionIdentifiers = infraClasseCollection.map(infraClasseItem => getInfraClasseIdentifier(infraClasseItem)!);
      const infraClassesToAdd = infraClasses.filter(infraClasseItem => {
        const infraClasseIdentifier = getInfraClasseIdentifier(infraClasseItem);
        if (infraClasseIdentifier == null || infraClasseCollectionIdentifiers.includes(infraClasseIdentifier)) {
          return false;
        }
        infraClasseCollectionIdentifiers.push(infraClasseIdentifier);
        return true;
      });
      return [...infraClassesToAdd, ...infraClasseCollection];
    }
    return infraClasseCollection;
  }
}
