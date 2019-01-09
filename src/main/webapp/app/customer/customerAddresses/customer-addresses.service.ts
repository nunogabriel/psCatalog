import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomerAddresses } from 'app/shared/customer/customer-addresses.model';

type EntityResponseType = HttpResponse<ICustomerAddresses>;
type EntityArrayResponseType = HttpResponse<ICustomerAddresses[]>;

@Injectable({ providedIn: 'root' })
export class CustomerAddressesService {
    public resourceUrl = SERVER_API_URL + 'api/customerAddresses';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/customerAddresses';

    constructor(private http: HttpClient) {}

    create(customerAddresses: ICustomerAddresses): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(customerAddresses);
        return this.http
            .post<ICustomerAddresses>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(customerAddresses: ICustomerAddresses): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(customerAddresses);
        return this.http
            .put<ICustomerAddresses>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICustomerAddresses>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomerAddresses[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICustomerAddresses[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(customerAddresses: ICustomerAddresses): ICustomerAddresses {
        const copy: ICustomerAddresses = Object.assign({}, customerAddresses, {
            createdDate: customerAddresses.createdDate != null && customerAddresses.createdDate.isValid() ? customerAddresses.createdDate.toJSON() : null,
            lastModifiedDate:
                customerAddresses.lastModifiedDate != null && customerAddresses.lastModifiedDate.isValid() ? customerAddresses.lastModifiedDate.toJSON() : null
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
            res.body.forEach((customerAddresses: ICustomerAddresses) => {
                customerAddresses.createdDate = customerAddresses.createdDate != null ? moment(customerAddresses.createdDate) : null;
                customerAddresses.lastModifiedDate = customerAddresses.lastModifiedDate != null ? moment(customerAddresses.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
