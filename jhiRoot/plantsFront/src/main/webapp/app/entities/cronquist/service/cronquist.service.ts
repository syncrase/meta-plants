import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICronquist, getCronquistIdentifier } from '../cronquist.model';

export type EntityResponseType = HttpResponse<ICronquist>;
export type EntityArrayResponseType = HttpResponse<ICronquist[]>;

@Injectable({ providedIn: 'root' })
export class CronquistService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cronquists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cronquist: ICronquist): Observable<EntityResponseType> {
    return this.http.post<ICronquist>(this.resourceUrl, cronquist, { observe: 'response' });
  }

  update(cronquist: ICronquist): Observable<EntityResponseType> {
    return this.http.put<ICronquist>(`${this.resourceUrl}/${getCronquistIdentifier(cronquist) as number}`, cronquist, {
      observe: 'response',
    });
  }

  partialUpdate(cronquist: ICronquist): Observable<EntityResponseType> {
    return this.http.patch<ICronquist>(`${this.resourceUrl}/${getCronquistIdentifier(cronquist) as number}`, cronquist, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICronquist>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICronquist[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCronquistToCollectionIfMissing(
    cronquistCollection: ICronquist[],
    ...cronquistsToCheck: (ICronquist | null | undefined)[]
  ): ICronquist[] {
    const cronquists: ICronquist[] = cronquistsToCheck.filter(isPresent);
    if (cronquists.length > 0) {
      const cronquistCollectionIdentifiers = cronquistCollection.map(cronquistItem => getCronquistIdentifier(cronquistItem)!);
      const cronquistsToAdd = cronquists.filter(cronquistItem => {
        const cronquistIdentifier = getCronquistIdentifier(cronquistItem);
        if (cronquistIdentifier == null || cronquistCollectionIdentifiers.includes(cronquistIdentifier)) {
          return false;
        }
        cronquistCollectionIdentifiers.push(cronquistIdentifier);
        return true;
      });
      return [...cronquistsToAdd, ...cronquistCollection];
    }
    return cronquistCollection;
  }
}
