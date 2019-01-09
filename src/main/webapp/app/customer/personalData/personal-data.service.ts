import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPersonalData } from 'app/shared/customer/personal-data.model';

type EntityResponseType = HttpResponse<IPersonalData>;
type EntityArrayResponseType = HttpResponse<IPersonalData[]>;

@Injectable({ providedIn: 'root' })
export class PersonalDataService {
    public resourceUrl = SERVER_API_URL + 'api/personalData';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/personalData';

    constructor(private http: HttpClient) {}

    create(personalData: IPersonalData): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(personalData);
        return this.http
            .post<IPersonalData>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(personalData: IPersonalData): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(personalData);
        return this.http
            .put<IPersonalData>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPersonalData>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPersonalData[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPersonalData[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(personalData: IPersonalData): IPersonalData {
        const copy: IPersonalData = Object.assign({}, personalData, {
            personalDataBeginDate:
                personalData.customerBeginDate != null && personalData.customerBeginDate.isValid() ? personalData.customerBeginDate.toJSON() : null,
            personalDataEndDate:
                personalData.customerEndDate != null && personalData.customerEndDate.isValid() ? personalData.customerEndDate.toJSON() : null,
            createdDate: personalData.createdDate != null && personalData.createdDate.isValid() ? personalData.createdDate.toJSON() : null,
            lastModifiedDate:
                personalData.lastModifiedDate != null && personalData.lastModifiedDate.isValid() ? personalData.lastModifiedDate.toJSON() : null
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
            res.body.forEach((personalData: IPersonalData) => {
                personalData.customerBeginDate = personalData.customerBeginDate != null ? moment(personalData.customerBeginDate) : null;
                personalData.customerEndDate = personalData.customerEndDate != null ? moment(personalData.customerEndDate) : null;
                personalData.createdDate = personalData.createdDate != null ? moment(personalData.createdDate) : null;
                personalData.lastModifiedDate = personalData.lastModifiedDate != null ? moment(personalData.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
