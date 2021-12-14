import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAPGIPlante, getAPGIPlanteIdentifier } from '../apgi-plante.model';

export type EntityResponseType = HttpResponse<IAPGIPlante>;
export type EntityArrayResponseType = HttpResponse<IAPGIPlante[]>;

@Injectable({ providedIn: 'root' })
export class APGIPlanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apgi-plantes', 'plantsms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aPGIPlante: IAPGIPlante): Observable<EntityResponseType> {
    return this.http.post<IAPGIPlante>(this.resourceUrl, aPGIPlante, { observe: 'response' });
  }

  update(aPGIPlante: IAPGIPlante): Observable<EntityResponseType> {
    return this.http.put<IAPGIPlante>(`${this.resourceUrl}/${getAPGIPlanteIdentifier(aPGIPlante) as number}`, aPGIPlante, {
      observe: 'response',
    });
  }

  partialUpdate(aPGIPlante: IAPGIPlante): Observable<EntityResponseType> {
    return this.http.patch<IAPGIPlante>(`${this.resourceUrl}/${getAPGIPlanteIdentifier(aPGIPlante) as number}`, aPGIPlante, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGIPlante>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGIPlante[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAPGIPlanteToCollectionIfMissing(
    aPGIPlanteCollection: IAPGIPlante[],
    ...aPGIPlantesToCheck: (IAPGIPlante | null | undefined)[]
  ): IAPGIPlante[] {
    const aPGIPlantes: IAPGIPlante[] = aPGIPlantesToCheck.filter(isPresent);
    if (aPGIPlantes.length > 0) {
      const aPGIPlanteCollectionIdentifiers = aPGIPlanteCollection.map(aPGIPlanteItem => getAPGIPlanteIdentifier(aPGIPlanteItem)!);
      const aPGIPlantesToAdd = aPGIPlantes.filter(aPGIPlanteItem => {
        const aPGIPlanteIdentifier = getAPGIPlanteIdentifier(aPGIPlanteItem);
        if (aPGIPlanteIdentifier == null || aPGIPlanteCollectionIdentifiers.includes(aPGIPlanteIdentifier)) {
          return false;
        }
        aPGIPlanteCollectionIdentifiers.push(aPGIPlanteIdentifier);
        return true;
      });
      return [...aPGIPlantesToAdd, ...aPGIPlanteCollection];
    }
    return aPGIPlanteCollection;
  }
}
