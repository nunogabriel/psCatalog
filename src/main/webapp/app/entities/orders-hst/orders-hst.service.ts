import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrdersHst } from 'app/shared/model/orders-hst.model';

type EntityResponseType = HttpResponse<IOrdersHst>;
type EntityArrayResponseType = HttpResponse<IOrdersHst[]>;

@Injectable({ providedIn: 'root' })
export class OrdersHstService {
    public resourceUrl = SERVER_API_URL + 'api/orders-hsts';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/orders-hsts';

    constructor(private http: HttpClient) {}

    create(ordersHst: IOrdersHst): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(ordersHst);
        return this.http
            .post<IOrdersHst>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(ordersHst: IOrdersHst): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(ordersHst);
        return this.http
            .put<IOrdersHst>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IOrdersHst>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrdersHst[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrdersHst[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(ordersHst: IOrdersHst): IOrdersHst {
        const copy: IOrdersHst = Object.assign({}, ordersHst, {
            orderDate: ordersHst.orderDate != null && ordersHst.orderDate.isValid() ? ordersHst.orderDate.toJSON() : null,
            deliveryDate: ordersHst.deliveryDate != null && ordersHst.deliveryDate.isValid() ? ordersHst.deliveryDate.toJSON() : null,
            createdDate: ordersHst.createdDate != null && ordersHst.createdDate.isValid() ? ordersHst.createdDate.toJSON() : null,
            lastModifiedDate:
                ordersHst.lastModifiedDate != null && ordersHst.lastModifiedDate.isValid() ? ordersHst.lastModifiedDate.toJSON() : null
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
            res.body.forEach((ordersHst: IOrdersHst) => {
                ordersHst.orderDate = ordersHst.orderDate != null ? moment(ordersHst.orderDate) : null;
                ordersHst.deliveryDate = ordersHst.deliveryDate != null ? moment(ordersHst.deliveryDate) : null;
                ordersHst.createdDate = ordersHst.createdDate != null ? moment(ordersHst.createdDate) : null;
                ordersHst.lastModifiedDate = ordersHst.lastModifiedDate != null ? moment(ordersHst.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
