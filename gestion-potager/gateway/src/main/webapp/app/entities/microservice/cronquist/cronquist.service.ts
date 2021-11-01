import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICronquist } from 'app/shared/model/microservice/cronquist.model';

type EntityResponseType = HttpResponse<ICronquist>;
type EntityArrayResponseType = HttpResponse<ICronquist[]>;

@Injectable({ providedIn: 'root' })
export class CronquistService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/cronquists';

  constructor(protected http: HttpClient) {}

  create(cronquist: ICronquist): Observable<EntityResponseType> {
    return this.http.post<ICronquist>(this.resourceUrl, cronquist, { observe: 'response' });
  }

  update(cronquist: ICronquist): Observable<EntityResponseType> {
    return this.http.put<ICronquist>(this.resourceUrl, cronquist, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICronquist>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICronquist[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
