import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrderDet } from 'app/shared/model/order-det.model';

type EntityResponseType = HttpResponse<IOrderDet>;
type EntityArrayResponseType = HttpResponse<IOrderDet[]>;

@Injectable({ providedIn: 'root' })
export class OrderDetService {
    public resourceUrl = SERVER_API_URL + 'api/order-dets';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/order-dets';

    constructor(private http: HttpClient) {}

    create(orderDet: IOrderDet): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(orderDet);
        return this.http
            .post<IOrderDet>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(orderDet: IOrderDet): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(orderDet);
        return this.http
            .put<IOrderDet>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IOrderDet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrderDet[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrderDet[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(orderDet: IOrderDet): IOrderDet {
        const copy: IOrderDet = Object.assign({}, orderDet, {
            createdDate: orderDet.createdDate != null && orderDet.createdDate.isValid() ? orderDet.createdDate.toJSON() : null,
            lastModifiedDate:
                orderDet.lastModifiedDate != null && orderDet.lastModifiedDate.isValid() ? orderDet.lastModifiedDate.toJSON() : null
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
            res.body.forEach((orderDet: IOrderDet) => {
                orderDet.createdDate = orderDet.createdDate != null ? moment(orderDet.createdDate) : null;
                orderDet.lastModifiedDate = orderDet.lastModifiedDate != null ? moment(orderDet.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
