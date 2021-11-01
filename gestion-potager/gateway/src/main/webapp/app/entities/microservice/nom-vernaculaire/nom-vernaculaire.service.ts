import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { INomVernaculaire } from 'app/shared/model/microservice/nom-vernaculaire.model';

type EntityResponseType = HttpResponse<INomVernaculaire>;
type EntityArrayResponseType = HttpResponse<INomVernaculaire[]>;

@Injectable({ providedIn: 'root' })
export class NomVernaculaireService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/nom-vernaculaires';

  constructor(protected http: HttpClient) {}

  create(nomVernaculaire: INomVernaculaire): Observable<EntityResponseType> {
    return this.http.post<INomVernaculaire>(this.resourceUrl, nomVernaculaire, { observe: 'response' });
  }

  update(nomVernaculaire: INomVernaculaire): Observable<EntityResponseType> {
    return this.http.put<INomVernaculaire>(this.resourceUrl, nomVernaculaire, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INomVernaculaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INomVernaculaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
