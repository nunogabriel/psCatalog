import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomerOrdersDet } from 'app/shared/customer/customer-orders-det.model';

type EntityResponseType = HttpResponse<ICustomerOrdersDet>;
type EntityArrayResponseType = HttpResponse<ICustomerOrdersDet[]>;

@Injectable({ providedIn: 'root' })
export class CustomerOrdersDetService {
    public resourceUrl = SERVER_API_URL + 'api/customer-orders-det';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/customer-orders-det';
    public resourceByOrderIdUrl = SERVER_API_URL + 'api/customer-orders-det/by-order-id';

    constructor(private http: HttpClient) {}

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICustomerOrdersDet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomerOrdersDet[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryByOrderId(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomerOrdersDet[]>(this.resourceByOrderIdUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomerOrdersDet[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getOrderTotal(): Observable<HttpResponse<any>> {
        return this.http
            .get<any>(`${this.resourceUrl}/orderTotal`, { observe: 'response' });
    }

    protected convertDateFromClient(customerOrdersDet: ICustomerOrdersDet): ICustomerOrdersDet {
        const copy: ICustomerOrdersDet = Object.assign({}, customerOrdersDet, {
            createdDate: customerOrdersDet.createdDate != null && customerOrdersDet.createdDate.isValid() ? customerOrdersDet.createdDate.toJSON() : null,
            lastModifiedDate:
                customerOrdersDet.lastModifiedDate != null && customerOrdersDet.lastModifiedDate.isValid() ? customerOrdersDet.lastModifiedDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
            res.body.lastModifiedDate = res.body.lastModifiedDate != null ? moment(res.body.lastModifiedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((customerOrdersDet: ICustomerOrdersDet) => {
                customerOrdersDet.createdDate = customerOrdersDet.createdDate != null ? moment(customerOrdersDet.createdDate) : null;
                customerOrdersDet.lastModifiedDate = customerOrdersDet.lastModifiedDate != null ? moment(customerOrdersDet.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
