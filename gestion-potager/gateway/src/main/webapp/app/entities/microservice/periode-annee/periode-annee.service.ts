import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPeriodeAnnee } from 'app/shared/model/microservice/periode-annee.model';

type EntityResponseType = HttpResponse<IPeriodeAnnee>;
type EntityArrayResponseType = HttpResponse<IPeriodeAnnee[]>;

@Injectable({ providedIn: 'root' })
export class PeriodeAnneeService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/periode-annees';

  constructor(protected http: HttpClient) {}

  create(periodeAnnee: IPeriodeAnnee): Observable<EntityResponseType> {
    return this.http.post<IPeriodeAnnee>(this.resourceUrl, periodeAnnee, { observe: 'response' });
  }

  update(periodeAnnee: IPeriodeAnnee): Observable<EntityResponseType> {
    return this.http.put<IPeriodeAnnee>(this.resourceUrl, periodeAnnee, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPeriodeAnnee>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPeriodeAnnee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
