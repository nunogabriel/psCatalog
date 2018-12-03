import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrderStatus } from 'app/shared/model/order-status.model';

type EntityResponseType = HttpResponse<IOrderStatus>;
type EntityArrayResponseType = HttpResponse<IOrderStatus[]>;

@Injectable({ providedIn: 'root' })
export class OrderStatusService {
    public resourceUrl = SERVER_API_URL + 'api/order-statuses';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/order-statuses';

    constructor(private http: HttpClient) {}

    create(orderStatus: IOrderStatus): Observable<EntityResponseType> {
        return this.http.post<IOrderStatus>(this.resourceUrl, orderStatus, { observe: 'response' });
    }

    update(orderStatus: IOrderStatus): Observable<EntityResponseType> {
        return this.http.put<IOrderStatus>(this.resourceUrl, orderStatus, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOrderStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrderStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrderStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
