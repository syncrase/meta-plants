import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousSection, getSousSectionIdentifier } from '../sous-section.model';

export type EntityResponseType = HttpResponse<ISousSection>;
export type EntityArrayResponseType = HttpResponse<ISousSection[]>;

@Injectable({ providedIn: 'root' })
export class SousSectionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-sections', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousSection: ISousSection): Observable<EntityResponseType> {
    return this.http.post<ISousSection>(this.resourceUrl, sousSection, { observe: 'response' });
  }

  update(sousSection: ISousSection): Observable<EntityResponseType> {
    return this.http.put<ISousSection>(`${this.resourceUrl}/${getSousSectionIdentifier(sousSection) as number}`, sousSection, {
      observe: 'response',
    });
  }

  partialUpdate(sousSection: ISousSection): Observable<EntityResponseType> {
    return this.http.patch<ISousSection>(`${this.resourceUrl}/${getSousSectionIdentifier(sousSection) as number}`, sousSection, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousSection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousSection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousSectionToCollectionIfMissing(
    sousSectionCollection: ISousSection[],
    ...sousSectionsToCheck: (ISousSection | null | undefined)[]
  ): ISousSection[] {
    const sousSections: ISousSection[] = sousSectionsToCheck.filter(isPresent);
    if (sousSections.length > 0) {
      const sousSectionCollectionIdentifiers = sousSectionCollection.map(sousSectionItem => getSousSectionIdentifier(sousSectionItem)!);
      const sousSectionsToAdd = sousSections.filter(sousSectionItem => {
        const sousSectionIdentifier = getSousSectionIdentifier(sousSectionItem);
        if (sousSectionIdentifier == null || sousSectionCollectionIdentifiers.includes(sousSectionIdentifier)) {
          return false;
        }
        sousSectionCollectionIdentifiers.push(sousSectionIdentifier);
        return true;
      });
      return [...sousSectionsToAdd, ...sousSectionCollection];
    }
    return sousSectionCollection;
  }
}
