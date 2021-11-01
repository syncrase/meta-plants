import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISol } from 'app/shared/model/microservice/sol.model';

type EntityResponseType = HttpResponse<ISol>;
type EntityArrayResponseType = HttpResponse<ISol[]>;

@Injectable({ providedIn: 'root' })
export class SolService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/sols';

  constructor(protected http: HttpClient) {}

  create(sol: ISol): Observable<EntityResponseType> {
    return this.http.post<ISol>(this.resourceUrl, sol, { observe: 'response' });
  }

  update(sol: ISol): Observable<EntityResponseType> {
    return this.http.put<ISol>(this.resourceUrl, sol, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISol>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISol[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
