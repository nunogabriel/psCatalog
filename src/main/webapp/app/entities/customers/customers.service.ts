import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomers } from 'app/shared/model/customers.model';

type EntityResponseType = HttpResponse<ICustomers>;
type EntityArrayResponseType = HttpResponse<ICustomers[]>;

@Injectable({ providedIn: 'root' })
export class CustomersService {
    public resourceUrl = SERVER_API_URL + 'api/customers';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/customers';

    constructor(private http: HttpClient) {}

    create(customers: ICustomers): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(customers);
        return this.http
            .post<ICustomers>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(customers: ICustomers): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(customers);
        return this.http
            .put<ICustomers>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICustomers>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomers[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomers[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(customers: ICustomers): ICustomers {
        const copy: ICustomers = Object.assign({}, customers, {
            customerBeginDate:
                customers.customerBeginDate != null && customers.customerBeginDate.isValid() ? customers.customerBeginDate.toJSON() : null,
            customerEndDate:
                customers.customerEndDate != null && customers.customerEndDate.isValid() ? customers.customerEndDate.toJSON() : null,
            createdDate: customers.createdDate != null && customers.createdDate.isValid() ? customers.createdDate.toJSON() : null,
            lastModifiedDate:
                customers.lastModifiedDate != null && customers.lastModifiedDate.isValid() ? customers.lastModifiedDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.customerBeginDate = res.body.customerBeginDate != null ? moment(res.body.customerBeginDate) : null;
            res.body.customerEndDate = res.body.customerEndDate != null ? moment(res.body.customerEndDate) : null;
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
            res.body.lastModifiedDate = res.body.lastModifiedDate != null ? moment(res.body.lastModifiedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((customers: ICustomers) => {
                customers.customerBeginDate = customers.customerBeginDate != null ? moment(customers.customerBeginDate) : null;
                customers.customerEndDate = customers.customerEndDate != null ? moment(customers.customerEndDate) : null;
                customers.createdDate = customers.createdDate != null ? moment(customers.createdDate) : null;
                customers.lastModifiedDate = customers.lastModifiedDate != null ? moment(customers.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
