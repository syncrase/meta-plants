import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICycleDeVie } from 'app/shared/model/microservice/cycle-de-vie.model';

type EntityResponseType = HttpResponse<ICycleDeVie>;
type EntityArrayResponseType = HttpResponse<ICycleDeVie[]>;

@Injectable({ providedIn: 'root' })
export class CycleDeVieService {
  public resourceUrl = SERVER_API_URL + 'services/microservice/api/cycle-de-vies';

  constructor(protected http: HttpClient) {}

  create(cycleDeVie: ICycleDeVie): Observable<EntityResponseType> {
    return this.http.post<ICycleDeVie>(this.resourceUrl, cycleDeVie, { observe: 'response' });
  }

  update(cycleDeVie: ICycleDeVie): Observable<EntityResponseType> {
    return this.http.put<ICycleDeVie>(this.resourceUrl, cycleDeVie, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICycleDeVie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICycleDeVie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
