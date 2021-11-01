import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAPGII } from 'app/shared/model/microservice/apgii.model';

type EntityResponseType = HttpResponse<IAPGII>;
type EntityArrayResponseType = HttpResponse<IAPGII[]>;

@Injectable({ providedIn: 'root' })
export class APGIIService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/apgiis';

  constructor(protected http: HttpClient) {}

  create(aPGII: IAPGII): Observable<EntityResponseType> {
    return this.http.post<IAPGII>(this.resourceUrl, aPGII, { observe: 'response' });
  }

  update(aPGII: IAPGII): Observable<EntityResponseType> {
    return this.http.put<IAPGII>(this.resourceUrl, aPGII, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAPGII>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAPGII[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
