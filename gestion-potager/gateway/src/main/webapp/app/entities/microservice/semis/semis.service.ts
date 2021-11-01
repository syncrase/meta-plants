import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISemis } from 'app/shared/model/microservice/semis.model';

type EntityResponseType = HttpResponse<ISemis>;
type EntityArrayResponseType = HttpResponse<ISemis[]>;

@Injectable({ providedIn: 'root' })
export class SemisService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/semis';

  constructor(protected http: HttpClient) {}

  create(semis: ISemis): Observable<EntityResponseType> {
    return this.http.post<ISemis>(this.resourceUrl, semis, { observe: 'response' });
  }

  update(semis: ISemis): Observable<EntityResponseType> {
    return this.http.put<ISemis>(this.resourceUrl, semis, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISemis>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISemis[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
