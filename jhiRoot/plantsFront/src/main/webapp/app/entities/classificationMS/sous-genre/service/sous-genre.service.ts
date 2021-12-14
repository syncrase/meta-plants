import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousGenre, getSousGenreIdentifier } from '../sous-genre.model';

export type EntityResponseType = HttpResponse<ISousGenre>;
export type EntityArrayResponseType = HttpResponse<ISousGenre[]>;

@Injectable({ providedIn: 'root' })
export class SousGenreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-genres', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousGenre: ISousGenre): Observable<EntityResponseType> {
    return this.http.post<ISousGenre>(this.resourceUrl, sousGenre, { observe: 'response' });
  }

  update(sousGenre: ISousGenre): Observable<EntityResponseType> {
    return this.http.put<ISousGenre>(`${this.resourceUrl}/${getSousGenreIdentifier(sousGenre) as number}`, sousGenre, {
      observe: 'response',
    });
  }

  partialUpdate(sousGenre: ISousGenre): Observable<EntityResponseType> {
    return this.http.patch<ISousGenre>(`${this.resourceUrl}/${getSousGenreIdentifier(sousGenre) as number}`, sousGenre, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousGenre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousGenre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousGenreToCollectionIfMissing(
    sousGenreCollection: ISousGenre[],
    ...sousGenresToCheck: (ISousGenre | null | undefined)[]
  ): ISousGenre[] {
    const sousGenres: ISousGenre[] = sousGenresToCheck.filter(isPresent);
    if (sousGenres.length > 0) {
      const sousGenreCollectionIdentifiers = sousGenreCollection.map(sousGenreItem => getSousGenreIdentifier(sousGenreItem)!);
      const sousGenresToAdd = sousGenres.filter(sousGenreItem => {
        const sousGenreIdentifier = getSousGenreIdentifier(sousGenreItem);
        if (sousGenreIdentifier == null || sousGenreCollectionIdentifiers.includes(sousGenreIdentifier)) {
          return false;
        }
        sousGenreCollectionIdentifiers.push(sousGenreIdentifier);
        return true;
      });
      return [...sousGenresToAdd, ...sousGenreCollection];
    }
    return sousGenreCollection;
  }
}
