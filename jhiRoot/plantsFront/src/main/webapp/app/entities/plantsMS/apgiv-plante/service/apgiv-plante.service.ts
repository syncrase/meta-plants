import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAPGIVPlante, getAPGIVPlanteIdentifier } from '../apgiv-plante.model';

export type EntityResponseType = HttpResponse<IAPGIVPlante>;
export type EntityArrayResponseType = HttpResponse<IAPGIVPlante[]>;

@Injectable({ providedIn: 'root' })
export class APGIVPlanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apgiv-plantes', 'plantsms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aPGIVPlante: IAPGIVPlante): Observable<EntityResponseType> {
    return this.http.post<IAPGIVPlante>(this.resourceUrl, aPGIVPlante, { observe: 'response' });
  }

  update(aPGIVPlante: IAPGIVPlante): Observable<EntityResponseType> {
    return this.http.put<IAPGIVPlante>(`${this.resourceUrl}/${getAPGIVPlanteIdentifier(aPGIVPlante) as number}`, aPGIVPlante, {
      observe: 'response',
    });
  }

  partialUpdate(aPGIVPlante: IAPGIVPlante): Observable<EntityResponseType> {
    return this.http.patch<IAPGIVPlante>(`${this.resourceUrl}/${getAPGIVPlanteIdentifier(aPGIVPlante) as number}`, aPGIVPlante, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGIVPlante>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGIVPlante[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAPGIVPlanteToCollectionIfMissing(
    aPGIVPlanteCollection: IAPGIVPlante[],
    ...aPGIVPlantesToCheck: (IAPGIVPlante | null | undefined)[]
  ): IAPGIVPlante[] {
    const aPGIVPlantes: IAPGIVPlante[] = aPGIVPlantesToCheck.filter(isPresent);
    if (aPGIVPlantes.length > 0) {
      const aPGIVPlanteCollectionIdentifiers = aPGIVPlanteCollection.map(aPGIVPlanteItem => getAPGIVPlanteIdentifier(aPGIVPlanteItem)!);
      const aPGIVPlantesToAdd = aPGIVPlantes.filter(aPGIVPlanteItem => {
        const aPGIVPlanteIdentifier = getAPGIVPlanteIdentifier(aPGIVPlanteItem);
        if (aPGIVPlanteIdentifier == null || aPGIVPlanteCollectionIdentifiers.includes(aPGIVPlanteIdentifier)) {
          return false;
        }
        aPGIVPlanteCollectionIdentifiers.push(aPGIVPlanteIdentifier);
        return true;
      });
      return [...aPGIVPlantesToAdd, ...aPGIVPlanteCollection];
    }
    return aPGIVPlanteCollection;
  }
}
