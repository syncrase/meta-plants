import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAPGIIIPlante, getAPGIIIPlanteIdentifier } from '../apgiii-plante.model';

export type EntityResponseType = HttpResponse<IAPGIIIPlante>;
export type EntityArrayResponseType = HttpResponse<IAPGIIIPlante[]>;

@Injectable({ providedIn: 'root' })
export class APGIIIPlanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apgiii-plantes', 'plantsms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aPGIIIPlante: IAPGIIIPlante): Observable<EntityResponseType> {
    return this.http.post<IAPGIIIPlante>(this.resourceUrl, aPGIIIPlante, { observe: 'response' });
  }

  update(aPGIIIPlante: IAPGIIIPlante): Observable<EntityResponseType> {
    return this.http.put<IAPGIIIPlante>(`${this.resourceUrl}/${getAPGIIIPlanteIdentifier(aPGIIIPlante) as number}`, aPGIIIPlante, {
      observe: 'response',
    });
  }

  partialUpdate(aPGIIIPlante: IAPGIIIPlante): Observable<EntityResponseType> {
    return this.http.patch<IAPGIIIPlante>(`${this.resourceUrl}/${getAPGIIIPlanteIdentifier(aPGIIIPlante) as number}`, aPGIIIPlante, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGIIIPlante>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGIIIPlante[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAPGIIIPlanteToCollectionIfMissing(
    aPGIIIPlanteCollection: IAPGIIIPlante[],
    ...aPGIIIPlantesToCheck: (IAPGIIIPlante | null | undefined)[]
  ): IAPGIIIPlante[] {
    const aPGIIIPlantes: IAPGIIIPlante[] = aPGIIIPlantesToCheck.filter(isPresent);
    if (aPGIIIPlantes.length > 0) {
      const aPGIIIPlanteCollectionIdentifiers = aPGIIIPlanteCollection.map(
        aPGIIIPlanteItem => getAPGIIIPlanteIdentifier(aPGIIIPlanteItem)!
      );
      const aPGIIIPlantesToAdd = aPGIIIPlantes.filter(aPGIIIPlanteItem => {
        const aPGIIIPlanteIdentifier = getAPGIIIPlanteIdentifier(aPGIIIPlanteItem);
        if (aPGIIIPlanteIdentifier == null || aPGIIIPlanteCollectionIdentifiers.includes(aPGIIIPlanteIdentifier)) {
          return false;
        }
        aPGIIIPlanteCollectionIdentifiers.push(aPGIIIPlanteIdentifier);
        return true;
      });
      return [...aPGIIIPlantesToAdd, ...aPGIIIPlanteCollection];
    }
    return aPGIIIPlanteCollection;
  }
}
