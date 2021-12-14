import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAPGIIPlante, getAPGIIPlanteIdentifier } from '../apgii-plante.model';

export type EntityResponseType = HttpResponse<IAPGIIPlante>;
export type EntityArrayResponseType = HttpResponse<IAPGIIPlante[]>;

@Injectable({ providedIn: 'root' })
export class APGIIPlanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apgii-plantes', 'plantsms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aPGIIPlante: IAPGIIPlante): Observable<EntityResponseType> {
    return this.http.post<IAPGIIPlante>(this.resourceUrl, aPGIIPlante, { observe: 'response' });
  }

  update(aPGIIPlante: IAPGIIPlante): Observable<EntityResponseType> {
    return this.http.put<IAPGIIPlante>(`${this.resourceUrl}/${getAPGIIPlanteIdentifier(aPGIIPlante) as number}`, aPGIIPlante, {
      observe: 'response',
    });
  }

  partialUpdate(aPGIIPlante: IAPGIIPlante): Observable<EntityResponseType> {
    return this.http.patch<IAPGIIPlante>(`${this.resourceUrl}/${getAPGIIPlanteIdentifier(aPGIIPlante) as number}`, aPGIIPlante, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGIIPlante>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGIIPlante[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAPGIIPlanteToCollectionIfMissing(
    aPGIIPlanteCollection: IAPGIIPlante[],
    ...aPGIIPlantesToCheck: (IAPGIIPlante | null | undefined)[]
  ): IAPGIIPlante[] {
    const aPGIIPlantes: IAPGIIPlante[] = aPGIIPlantesToCheck.filter(isPresent);
    if (aPGIIPlantes.length > 0) {
      const aPGIIPlanteCollectionIdentifiers = aPGIIPlanteCollection.map(aPGIIPlanteItem => getAPGIIPlanteIdentifier(aPGIIPlanteItem)!);
      const aPGIIPlantesToAdd = aPGIIPlantes.filter(aPGIIPlanteItem => {
        const aPGIIPlanteIdentifier = getAPGIIPlanteIdentifier(aPGIIPlanteItem);
        if (aPGIIPlanteIdentifier == null || aPGIIPlanteCollectionIdentifiers.includes(aPGIIPlanteIdentifier)) {
          return false;
        }
        aPGIIPlanteCollectionIdentifiers.push(aPGIIPlanteIdentifier);
        return true;
      });
      return [...aPGIIPlantesToAdd, ...aPGIIPlanteCollection];
    }
    return aPGIIPlanteCollection;
  }
}
