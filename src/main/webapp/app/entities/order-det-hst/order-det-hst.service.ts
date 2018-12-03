import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrderDetHst } from 'app/shared/model/order-det-hst.model';

type EntityResponseType = HttpResponse<IOrderDetHst>;
type EntityArrayResponseType = HttpResponse<IOrderDetHst[]>;

@Injectable({ providedIn: 'root' })
export class OrderDetHstService {
    public resourceUrl = SERVER_API_URL + 'api/order-det-hsts';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/order-det-hsts';

    constructor(private http: HttpClient) {}

    create(orderDetHst: IOrderDetHst): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(orderDetHst);
        return this.http
            .post<IOrderDetHst>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(orderDetHst: IOrderDetHst): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(orderDetHst);
        return this.http
            .put<IOrderDetHst>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IOrderDetHst>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrderDetHst[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrderDetHst[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(orderDetHst: IOrderDetHst): IOrderDetHst {
        const copy: IOrderDetHst = Object.assign({}, orderDetHst, {
            createdDate: orderDetHst.createdDate != null && orderDetHst.createdDate.isValid() ? orderDetHst.createdDate.toJSON() : null,
            lastModifiedDate:
                orderDetHst.lastModifiedDate != null && orderDetHst.lastModifiedDate.isValid()
                    ? orderDetHst.lastModifiedDate.toJSON()
                    : null
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
            res.body.forEach((orderDetHst: IOrderDetHst) => {
                orderDetHst.createdDate = orderDetHst.createdDate != null ? moment(orderDetHst.createdDate) : null;
                orderDetHst.lastModifiedDate = orderDetHst.lastModifiedDate != null ? moment(orderDetHst.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
