import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEspece, getEspeceIdentifier } from '../espece.model';

export type EntityResponseType = HttpResponse<IEspece>;
export type EntityArrayResponseType = HttpResponse<IEspece[]>;

@Injectable({ providedIn: 'root' })
export class EspeceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/especes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(espece: IEspece): Observable<EntityResponseType> {
    return this.http.post<IEspece>(this.resourceUrl, espece, { observe: 'response' });
  }

  update(espece: IEspece): Observable<EntityResponseType> {
    return this.http.put<IEspece>(`${this.resourceUrl}/${getEspeceIdentifier(espece) as number}`, espece, { observe: 'response' });
  }

  partialUpdate(espece: IEspece): Observable<EntityResponseType> {
    return this.http.patch<IEspece>(`${this.resourceUrl}/${getEspeceIdentifier(espece) as number}`, espece, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEspece>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEspece[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEspeceToCollectionIfMissing(especeCollection: IEspece[], ...especesToCheck: (IEspece | null | undefined)[]): IEspece[] {
    const especes: IEspece[] = especesToCheck.filter(isPresent);
    if (especes.length > 0) {
      const especeCollectionIdentifiers = especeCollection.map(especeItem => getEspeceIdentifier(especeItem)!);
      const especesToAdd = especes.filter(especeItem => {
        const especeIdentifier = getEspeceIdentifier(especeItem);
        if (especeIdentifier == null || especeCollectionIdentifiers.includes(especeIdentifier)) {
          return false;
        }
        especeCollectionIdentifiers.push(especeIdentifier);
        return true;
      });
      return [...especesToAdd, ...especeCollection];
    }
    return especeCollection;
  }
}
