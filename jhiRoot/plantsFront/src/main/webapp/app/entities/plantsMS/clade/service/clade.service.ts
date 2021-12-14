import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClade, getCladeIdentifier } from '../clade.model';

export type EntityResponseType = HttpResponse<IClade>;
export type EntityArrayResponseType = HttpResponse<IClade[]>;

@Injectable({ providedIn: 'root' })
export class CladeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clades', 'plantsms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(clade: IClade): Observable<EntityResponseType> {
    return this.http.post<IClade>(this.resourceUrl, clade, { observe: 'response' });
  }

  update(clade: IClade): Observable<EntityResponseType> {
    return this.http.put<IClade>(`${this.resourceUrl}/${getCladeIdentifier(clade) as number}`, clade, { observe: 'response' });
  }

  partialUpdate(clade: IClade): Observable<EntityResponseType> {
    return this.http.patch<IClade>(`${this.resourceUrl}/${getCladeIdentifier(clade) as number}`, clade, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClade>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClade[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCladeToCollectionIfMissing(cladeCollection: IClade[], ...cladesToCheck: (IClade | null | undefined)[]): IClade[] {
    const clades: IClade[] = cladesToCheck.filter(isPresent);
    if (clades.length > 0) {
      const cladeCollectionIdentifiers = cladeCollection.map(cladeItem => getCladeIdentifier(cladeItem)!);
      const cladesToAdd = clades.filter(cladeItem => {
        const cladeIdentifier = getCladeIdentifier(cladeItem);
        if (cladeIdentifier == null || cladeCollectionIdentifiers.includes(cladeIdentifier)) {
          return false;
        }
        cladeCollectionIdentifiers.push(cladeIdentifier);
        return true;
      });
      return [...cladesToAdd, ...cladeCollection];
    }
    return cladeCollection;
  }
}
