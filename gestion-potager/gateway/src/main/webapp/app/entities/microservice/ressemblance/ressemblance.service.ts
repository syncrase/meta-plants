import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRessemblance } from 'app/shared/model/microservice/ressemblance.model';

type EntityResponseType = HttpResponse<IRessemblance>;
type EntityArrayResponseType = HttpResponse<IRessemblance[]>;

@Injectable({ providedIn: 'root' })
export class RessemblanceService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/ressemblances';

  constructor(protected http: HttpClient) {}

  create(ressemblance: IRessemblance): Observable<EntityResponseType> {
    return this.http.post<IRessemblance>(this.resourceUrl, ressemblance, { observe: 'response' });
  }

  update(ressemblance: IRessemblance): Observable<EntityResponseType> {
    return this.http.put<IRessemblance>(this.resourceUrl, ressemblance, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRessemblance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRessemblance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
