import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISuppliers } from 'app/shared/model/suppliers.model';

type EntityResponseType = HttpResponse<ISuppliers>;
type EntityArrayResponseType = HttpResponse<ISuppliers[]>;

@Injectable({ providedIn: 'root' })
export class SuppliersService {
    public resourceUrl = SERVER_API_URL + 'api/suppliers';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/suppliers';

    constructor(private http: HttpClient) {}

    create(suppliers: ISuppliers): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(suppliers);
        return this.http
            .post<ISuppliers>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(suppliers: ISuppliers): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(suppliers);
        return this.http
            .put<ISuppliers>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISuppliers>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISuppliers[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISuppliers[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(suppliers: ISuppliers): ISuppliers {
        const copy: ISuppliers = Object.assign({}, suppliers, {
            supplierBeginDate:
                suppliers.supplierBeginDate != null && suppliers.supplierBeginDate.isValid() ? suppliers.supplierBeginDate.toJSON() : null,
            supplierEndDate:
                suppliers.supplierEndDate != null && suppliers.supplierEndDate.isValid() ? suppliers.supplierEndDate.toJSON() : null,
            createdDate: suppliers.createdDate != null && suppliers.createdDate.isValid() ? suppliers.createdDate.toJSON() : null,
            lastModifiedDate:
                suppliers.lastModifiedDate != null && suppliers.lastModifiedDate.isValid() ? suppliers.lastModifiedDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.supplierBeginDate = res.body.supplierBeginDate != null ? moment(res.body.supplierBeginDate) : null;
            res.body.supplierEndDate = res.body.supplierEndDate != null ? moment(res.body.supplierEndDate) : null;
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
            res.body.lastModifiedDate = res.body.lastModifiedDate != null ? moment(res.body.lastModifiedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((suppliers: ISuppliers) => {
                suppliers.supplierBeginDate = suppliers.supplierBeginDate != null ? moment(suppliers.supplierBeginDate) : null;
                suppliers.supplierEndDate = suppliers.supplierEndDate != null ? moment(suppliers.supplierEndDate) : null;
                suppliers.createdDate = suppliers.createdDate != null ? moment(suppliers.createdDate) : null;
                suppliers.lastModifiedDate = suppliers.lastModifiedDate != null ? moment(suppliers.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
