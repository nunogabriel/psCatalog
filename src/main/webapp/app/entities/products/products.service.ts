import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProducts } from 'app/shared/model/products.model';

type EntityResponseType = HttpResponse<IProducts>;
type EntityArrayResponseType = HttpResponse<IProducts[]>;

@Injectable({ providedIn: 'root' })
export class ProductsService {
    public resourceUrl = SERVER_API_URL + 'api/products';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/products';

    constructor(private http: HttpClient) {}

    create(products: IProducts): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(products);
        return this.http
            .post<IProducts>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(products: IProducts): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(products);
        return this.http
            .put<IProducts>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IProducts>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IProducts[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IProducts[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(products: IProducts): IProducts {
        const copy: IProducts = Object.assign({}, products, {
            productStartDate:
                products.productStartDate != null && products.productStartDate.isValid() ? products.productStartDate.toJSON() : null,
            productEndDate: products.productEndDate != null && products.productEndDate.isValid() ? products.productEndDate.toJSON() : null,
            createdDate: products.createdDate != null && products.createdDate.isValid() ? products.createdDate.toJSON() : null,
            lastModifiedDate:
                products.lastModifiedDate != null && products.lastModifiedDate.isValid() ? products.lastModifiedDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.productStartDate = res.body.productStartDate != null ? moment(res.body.productStartDate) : null;
            res.body.productEndDate = res.body.productEndDate != null ? moment(res.body.productEndDate) : null;
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
            res.body.lastModifiedDate = res.body.lastModifiedDate != null ? moment(res.body.lastModifiedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((products: IProducts) => {
                products.productStartDate = products.productStartDate != null ? moment(products.productStartDate) : null;
                products.productEndDate = products.productEndDate != null ? moment(products.productEndDate) : null;
                products.createdDate = products.createdDate != null ? moment(products.createdDate) : null;
                products.lastModifiedDate = products.lastModifiedDate != null ? moment(products.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
