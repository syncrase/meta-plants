import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVariete, getVarieteIdentifier } from '../variete.model';

export type EntityResponseType = HttpResponse<IVariete>;
export type EntityArrayResponseType = HttpResponse<IVariete[]>;

@Injectable({ providedIn: 'root' })
export class VarieteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/varietes', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(variete: IVariete): Observable<EntityResponseType> {
    return this.http.post<IVariete>(this.resourceUrl, variete, { observe: 'response' });
  }

  update(variete: IVariete): Observable<EntityResponseType> {
    return this.http.put<IVariete>(`${this.resourceUrl}/${getVarieteIdentifier(variete) as number}`, variete, { observe: 'response' });
  }

  partialUpdate(variete: IVariete): Observable<EntityResponseType> {
    return this.http.patch<IVariete>(`${this.resourceUrl}/${getVarieteIdentifier(variete) as number}`, variete, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVariete>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVariete[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVarieteToCollectionIfMissing(varieteCollection: IVariete[], ...varietesToCheck: (IVariete | null | undefined)[]): IVariete[] {
    const varietes: IVariete[] = varietesToCheck.filter(isPresent);
    if (varietes.length > 0) {
      const varieteCollectionIdentifiers = varieteCollection.map(varieteItem => getVarieteIdentifier(varieteItem)!);
      const varietesToAdd = varietes.filter(varieteItem => {
        const varieteIdentifier = getVarieteIdentifier(varieteItem);
        if (varieteIdentifier == null || varieteCollectionIdentifiers.includes(varieteIdentifier)) {
          return false;
        }
        varieteCollectionIdentifiers.push(varieteIdentifier);
        return true;
      });
      return [...varietesToAdd, ...varieteCollection];
    }
    return varieteCollection;
  }
}
