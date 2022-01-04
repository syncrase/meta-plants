import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassificationNom, getClassificationNomIdentifier } from '../classification-nom.model';

export type EntityResponseType = HttpResponse<IClassificationNom>;
export type EntityArrayResponseType = HttpResponse<IClassificationNom[]>;

@Injectable({ providedIn: 'root' })
export class ClassificationNomService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/classification-noms', 'classificationms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(classificationNom: IClassificationNom): Observable<EntityResponseType> {
    return this.http.post<IClassificationNom>(this.resourceUrl, classificationNom, { observe: 'response' });
  }

  update(classificationNom: IClassificationNom): Observable<EntityResponseType> {
    return this.http.put<IClassificationNom>(
      `${this.resourceUrl}/${getClassificationNomIdentifier(classificationNom) as number}`,
      classificationNom,
      { observe: 'response' }
    );
  }

  partialUpdate(classificationNom: IClassificationNom): Observable<EntityResponseType> {
    return this.http.patch<IClassificationNom>(
      `${this.resourceUrl}/${getClassificationNomIdentifier(classificationNom) as number}`,
      classificationNom,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassificationNom>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassificationNom[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassificationNomToCollectionIfMissing(
    classificationNomCollection: IClassificationNom[],
    ...classificationNomsToCheck: (IClassificationNom | null | undefined)[]
  ): IClassificationNom[] {
    const classificationNoms: IClassificationNom[] = classificationNomsToCheck.filter(isPresent);
    if (classificationNoms.length > 0) {
      const classificationNomCollectionIdentifiers = classificationNomCollection.map(
        classificationNomItem => getClassificationNomIdentifier(classificationNomItem)!
      );
      const classificationNomsToAdd = classificationNoms.filter(classificationNomItem => {
        const classificationNomIdentifier = getClassificationNomIdentifier(classificationNomItem);
        if (classificationNomIdentifier == null || classificationNomCollectionIdentifiers.includes(classificationNomIdentifier)) {
          return false;
        }
        classificationNomCollectionIdentifiers.push(classificationNomIdentifier);
        return true;
      });
      return [...classificationNomsToAdd, ...classificationNomCollection];
    }
    return classificationNomCollection;
  }
}
