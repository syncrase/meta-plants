import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMois } from 'app/shared/model/microservice/mois.model';

type EntityResponseType = HttpResponse<IMois>;
type EntityArrayResponseType = HttpResponse<IMois[]>;

@Injectable({ providedIn: 'root' })
export class MoisService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/mois';

  constructor(protected http: HttpClient) {}

  create(mois: IMois): Observable<EntityResponseType> {
    return this.http.post<IMois>(this.resourceUrl, mois, { observe: 'response' });
  }

  update(mois: IMois): Observable<EntityResponseType> {
    return this.http.put<IMois>(this.resourceUrl, mois, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMois>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMois[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
