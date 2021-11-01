import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAPGIII } from 'app/shared/model/microservice/apgiii.model';

type EntityResponseType = HttpResponse<IAPGIII>;
type EntityArrayResponseType = HttpResponse<IAPGIII[]>;

@Injectable({ providedIn: 'root' })
export class APGIIIService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/apgiiis';

  constructor(protected http: HttpClient) {}

  create(aPGIII: IAPGIII): Observable<EntityResponseType> {
    return this.http.post<IAPGIII>(this.resourceUrl, aPGIII, { observe: 'response' });
  }

  update(aPGIII: IAPGIII): Observable<EntityResponseType> {
    return this.http.put<IAPGIII>(this.resourceUrl, aPGIII, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGIII>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGIII[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
