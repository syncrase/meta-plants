import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IForme, getFormeIdentifier } from '../forme.model';

export type EntityResponseType = HttpResponse<IForme>;
export type EntityArrayResponseType = HttpResponse<IForme[]>;

@Injectable({ providedIn: 'root' })
export class FormeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(forme: IForme): Observable<EntityResponseType> {
    return this.http.post<IForme>(this.resourceUrl, forme, { observe: 'response' });
  }

  update(forme: IForme): Observable<EntityResponseType> {
    return this.http.put<IForme>(`${this.resourceUrl}/${getFormeIdentifier(forme) as number}`, forme, { observe: 'response' });
  }

  partialUpdate(forme: IForme): Observable<EntityResponseType> {
    return this.http.patch<IForme>(`${this.resourceUrl}/${getFormeIdentifier(forme) as number}`, forme, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IForme>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IForme[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormeToCollectionIfMissing(formeCollection: IForme[], ...formesToCheck: (IForme | null | undefined)[]): IForme[] {
    const formes: IForme[] = formesToCheck.filter(isPresent);
    if (formes.length > 0) {
      const formeCollectionIdentifiers = formeCollection.map(formeItem => getFormeIdentifier(formeItem)!);
      const formesToAdd = formes.filter(formeItem => {
        const formeIdentifier = getFormeIdentifier(formeItem);
        if (formeIdentifier == null || formeCollectionIdentifiers.includes(formeIdentifier)) {
          return false;
        }
        formeCollectionIdentifiers.push(formeIdentifier);
        return true;
      });
      return [...formesToAdd, ...formeCollection];
    }
    return formeCollection;
  }
}
