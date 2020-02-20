import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IRestaurant } from 'app/shared/model/restaurant.model';

type EntityResponseType = HttpResponse<IRestaurant>;
type EntityArrayResponseType = HttpResponse<IRestaurant[]>;

@Injectable({ providedIn: 'root' })
export class RestaurantService {
  public resourceUrl = SERVER_API_URL + 'api/restaurants';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/restaurants';

  constructor(protected http: HttpClient) {}

  create(restaurant: IRestaurant): Observable<EntityResponseType> {
    return this.http.post<IRestaurant>(this.resourceUrl, restaurant, { observe: 'response' });
  }

  update(restaurant: IRestaurant): Observable<EntityResponseType> {
    return this.http.put<IRestaurant>(this.resourceUrl, restaurant, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRestaurant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRestaurant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRestaurant[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
