import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAPGI } from 'app/shared/model/microservice/apgi.model';

type EntityResponseType = HttpResponse<IAPGI>;
type EntityArrayResponseType = HttpResponse<IAPGI[]>;

@Injectable({ providedIn: 'root' })
export class APGIService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/apgis';

  constructor(protected http: HttpClient) {}

  create(aPGI: IAPGI): Observable<EntityResponseType> {
    return this.http.post<IAPGI>(this.resourceUrl, aPGI, { observe: 'response' });
  }

  update(aPGI: IAPGI): Observable<EntityResponseType> {
    return this.http.put<IAPGI>(this.resourceUrl, aPGI, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGI>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGI[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
