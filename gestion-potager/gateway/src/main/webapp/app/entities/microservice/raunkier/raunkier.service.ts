import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRaunkier } from 'app/shared/model/microservice/raunkier.model';

type EntityResponseType = HttpResponse<IRaunkier>;
type EntityArrayResponseType = HttpResponse<IRaunkier[]>;

@Injectable({ providedIn: 'root' })
export class RaunkierService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/raunkiers';

  constructor(protected http: HttpClient) {}

  create(raunkier: IRaunkier): Observable<EntityResponseType> {
    return this.http.post<IRaunkier>(this.resourceUrl, raunkier, { observe: 'response' });
  }

  update(raunkier: IRaunkier): Observable<EntityResponseType> {
    return this.http.put<IRaunkier>(this.resourceUrl, raunkier, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRaunkier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRaunkier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
