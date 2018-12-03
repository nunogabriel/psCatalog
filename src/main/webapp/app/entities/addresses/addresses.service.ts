import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAddresses } from 'app/shared/model/addresses.model';

type EntityResponseType = HttpResponse<IAddresses>;
type EntityArrayResponseType = HttpResponse<IAddresses[]>;

@Injectable({ providedIn: 'root' })
export class AddressesService {
    public resourceUrl = SERVER_API_URL + 'api/addresses';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/addresses';

    constructor(private http: HttpClient) {}

    create(addresses: IAddresses): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(addresses);
        return this.http
            .post<IAddresses>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(addresses: IAddresses): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(addresses);
        return this.http
            .put<IAddresses>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IAddresses>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAddresses[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAddresses[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(addresses: IAddresses): IAddresses {
        const copy: IAddresses = Object.assign({}, addresses, {
            createdDate: addresses.createdDate != null && addresses.createdDate.isValid() ? addresses.createdDate.toJSON() : null,
            lastModifiedDate:
                addresses.lastModifiedDate != null && addresses.lastModifiedDate.isValid() ? addresses.lastModifiedDate.toJSON() : null
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
            res.body.forEach((addresses: IAddresses) => {
                addresses.createdDate = addresses.createdDate != null ? moment(addresses.createdDate) : null;
                addresses.lastModifiedDate = addresses.lastModifiedDate != null ? moment(addresses.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
