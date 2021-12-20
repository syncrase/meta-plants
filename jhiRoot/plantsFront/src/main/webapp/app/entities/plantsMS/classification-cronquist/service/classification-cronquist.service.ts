import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassificationCronquist, getClassificationCronquistIdentifier } from '../classification-cronquist.model';

export type EntityResponseType = HttpResponse<IClassificationCronquist>;
export type EntityArrayResponseType = HttpResponse<IClassificationCronquist[]>;

@Injectable({ providedIn: 'root' })
export class ClassificationCronquistService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/classification-cronquists', 'plantsms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(classificationCronquist: IClassificationCronquist): Observable<EntityResponseType> {
    return this.http.post<IClassificationCronquist>(this.resourceUrl, classificationCronquist, { observe: 'response' });
  }

  update(classificationCronquist: IClassificationCronquist): Observable<EntityResponseType> {
    return this.http.put<IClassificationCronquist>(
      `${this.resourceUrl}/${getClassificationCronquistIdentifier(classificationCronquist) as number}`,
      classificationCronquist,
      { observe: 'response' }
    );
  }

  partialUpdate(classificationCronquist: IClassificationCronquist): Observable<EntityResponseType> {
    return this.http.patch<IClassificationCronquist>(
      `${this.resourceUrl}/${getClassificationCronquistIdentifier(classificationCronquist) as number}`,
      classificationCronquist,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassificationCronquist>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassificationCronquist[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassificationCronquistToCollectionIfMissing(
    classificationCronquistCollection: IClassificationCronquist[],
    ...classificationCronquistsToCheck: (IClassificationCronquist | null | undefined)[]
  ): IClassificationCronquist[] {
    const classificationCronquists: IClassificationCronquist[] = classificationCronquistsToCheck.filter(isPresent);
    if (classificationCronquists.length > 0) {
      const classificationCronquistCollectionIdentifiers = classificationCronquistCollection.map(
        classificationCronquistItem => getClassificationCronquistIdentifier(classificationCronquistItem)!
      );
      const classificationCronquistsToAdd = classificationCronquists.filter(classificationCronquistItem => {
        const classificationCronquistIdentifier = getClassificationCronquistIdentifier(classificationCronquistItem);
        if (
          classificationCronquistIdentifier == null ||
          classificationCronquistCollectionIdentifiers.includes(classificationCronquistIdentifier)
        ) {
          return false;
        }
        classificationCronquistCollectionIdentifiers.push(classificationCronquistIdentifier);
        return true;
      });
      return [...classificationCronquistsToAdd, ...classificationCronquistCollection];
    }
    return classificationCronquistCollection;
  }
}
