import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICart } from 'app/shared/cart/cart.model';

type EntityResponseType = HttpResponse<ICart>;
type EntityArrayResponseType = HttpResponse<ICart[]>;

@Injectable({ providedIn: 'root' })
export class CartOrderDetService {
    public resourceUrl = SERVER_API_URL + 'api/cart-order-det';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/cart-order-det';

    constructor(private http: HttpClient) {}

    create(cartOrderDet: ICart): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cartOrderDet);
        return this.http
            .post<ICart>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(cartOrderDet: ICart): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cartOrderDet);
        return this.http
            .put<ICart>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICart>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICart[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    order(): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/order`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICart[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(cartOrderDet: ICart): ICart {
        const copy: ICart = Object.assign({}, cartOrderDet, {
            createdDate: cartOrderDet.createdDate != null && cartOrderDet.createdDate.isValid() ? cartOrderDet.createdDate.toJSON() : null,
            lastModifiedDate:
                cartOrderDet.lastModifiedDate != null && cartOrderDet.lastModifiedDate.isValid() ? cartOrderDet.lastModifiedDate.toJSON() : null
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
            res.body.forEach((cartOrderDet: ICart) => {
                cartOrderDet.createdDate = cartOrderDet.createdDate != null ? moment(cartOrderDet.createdDate) : null;
                cartOrderDet.lastModifiedDate = cartOrderDet.lastModifiedDate != null ? moment(cartOrderDet.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
