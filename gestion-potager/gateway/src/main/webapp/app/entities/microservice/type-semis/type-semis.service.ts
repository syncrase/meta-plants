import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITypeSemis } from 'app/shared/model/microservice/type-semis.model';

type EntityResponseType = HttpResponse<ITypeSemis>;
type EntityArrayResponseType = HttpResponse<ITypeSemis[]>;

@Injectable({ providedIn: 'root' })
export class TypeSemisService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/type-semis';

  constructor(protected http: HttpClient) {}

  create(typeSemis: ITypeSemis): Observable<EntityResponseType> {
    return this.http.post<ITypeSemis>(this.resourceUrl, typeSemis, { observe: 'response' });
  }

  update(typeSemis: ITypeSemis): Observable<EntityResponseType> {
    return this.http.put<ITypeSemis>(this.resourceUrl, typeSemis, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeSemis>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeSemis[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
