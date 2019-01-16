import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomerOrders } from 'app/shared/customer/customer-orders.model';

type EntityResponseType = HttpResponse<ICustomerOrders>;
type EntityArrayResponseType = HttpResponse<ICustomerOrders[]>;

@Injectable({ providedIn: 'root' })
export class CustomerOrdersService {
    public resourceUrl = SERVER_API_URL + 'api/customer-orders';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/customer-orders';

    constructor(private http: HttpClient) {}

    update(customerOrders: ICustomerOrders): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(customerOrders);
        return this.http
            .put<ICustomerOrders>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICustomerOrders>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomerOrders[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomerOrders[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(customerOrders: ICustomerOrders): ICustomerOrders {
        const copy: ICustomerOrders = Object.assign({}, customerOrders, {
            orderDate: customerOrders.orderDate != null && customerOrders.orderDate.isValid() ? customerOrders.orderDate.toJSON() : null,
            deliveryDate: customerOrders.deliveryDate != null && customerOrders.deliveryDate.isValid() ? customerOrders.deliveryDate.toJSON() : null,
            createdDate: customerOrders.createdDate != null && customerOrders.createdDate.isValid() ? customerOrders.createdDate.toJSON() : null,
            lastModifiedDate: customerOrders.lastModifiedDate != null && customerOrders.lastModifiedDate.isValid() ? customerOrders.lastModifiedDate.toJSON() : null
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
            res.body.forEach((customerOrders: ICustomerOrders) => {
                customerOrders.orderDate = customerOrders.orderDate != null ? moment(customerOrders.orderDate) : null;
                customerOrders.deliveryDate = customerOrders.deliveryDate != null ? moment(customerOrders.deliveryDate) : null;
                customerOrders.createdDate = customerOrders.createdDate != null ? moment(customerOrders.createdDate) : null;
                customerOrders.lastModifiedDate = customerOrders.lastModifiedDate != null ? moment(customerOrders.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
