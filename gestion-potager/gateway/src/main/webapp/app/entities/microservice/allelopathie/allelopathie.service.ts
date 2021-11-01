import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAllelopathie } from 'app/shared/model/microservice/allelopathie.model';

type EntityResponseType = HttpResponse<IAllelopathie>;
type EntityArrayResponseType = HttpResponse<IAllelopathie[]>;

@Injectable({ providedIn: 'root' })
export class AllelopathieService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/allelopathies';

  constructor(protected http: HttpClient) {}

  create(allelopathie: IAllelopathie): Observable<EntityResponseType> {
    return this.http.post<IAllelopathie>(this.resourceUrl, allelopathie, { observe: 'response' });
  }

  update(allelopathie: IAllelopathie): Observable<EntityResponseType> {
    return this.http.put<IAllelopathie>(this.resourceUrl, allelopathie, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAllelopathie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAllelopathie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
