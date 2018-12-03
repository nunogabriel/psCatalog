import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrders } from 'app/shared/model/orders.model';

type EntityResponseType = HttpResponse<IOrders>;
type EntityArrayResponseType = HttpResponse<IOrders[]>;

@Injectable({ providedIn: 'root' })
export class OrdersService {
    public resourceUrl = SERVER_API_URL + 'api/orders';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/orders';

    constructor(private http: HttpClient) {}

    create(orders: IOrders): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(orders);
        return this.http
            .post<IOrders>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(orders: IOrders): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(orders);
        return this.http
            .put<IOrders>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IOrders>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrders[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrders[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(orders: IOrders): IOrders {
        const copy: IOrders = Object.assign({}, orders, {
            orderDate: orders.orderDate != null && orders.orderDate.isValid() ? orders.orderDate.toJSON() : null,
            deliveryDate: orders.deliveryDate != null && orders.deliveryDate.isValid() ? orders.deliveryDate.toJSON() : null,
            createdDate: orders.createdDate != null && orders.createdDate.isValid() ? orders.createdDate.toJSON() : null,
            lastModifiedDate: orders.lastModifiedDate != null && orders.lastModifiedDate.isValid() ? orders.lastModifiedDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.orderDate = res.body.orderDate != null ? moment(res.body.orderDate) : null;
            res.body.deliveryDate = res.body.deliveryDate != null ? moment(res.body.deliveryDate) : null;
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
            res.body.lastModifiedDate = res.body.lastModifiedDate != null ? moment(res.body.lastModifiedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((orders: IOrders) => {
                orders.orderDate = orders.orderDate != null ? moment(orders.orderDate) : null;
                orders.deliveryDate = orders.deliveryDate != null ? moment(orders.deliveryDate) : null;
                orders.createdDate = orders.createdDate != null ? moment(orders.createdDate) : null;
                orders.lastModifiedDate = orders.lastModifiedDate != null ? moment(orders.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
