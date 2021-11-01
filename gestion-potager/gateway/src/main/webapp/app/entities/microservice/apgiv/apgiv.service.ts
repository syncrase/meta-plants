import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAPGIV } from 'app/shared/model/microservice/apgiv.model';

type EntityResponseType = HttpResponse<IAPGIV>;
type EntityArrayResponseType = HttpResponse<IAPGIV[]>;

@Injectable({ providedIn: 'root' })
export class APGIVService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/apgivs';

  constructor(protected http: HttpClient) {}

  create(aPGIV: IAPGIV): Observable<EntityResponseType> {
    return this.http.post<IAPGIV>(this.resourceUrl, aPGIV, { observe: 'response' });
  }

  update(aPGIV: IAPGIV): Observable<EntityResponseType> {
    return this.http.put<IAPGIV>(this.resourceUrl, aPGIV, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGIV>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGIV[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
