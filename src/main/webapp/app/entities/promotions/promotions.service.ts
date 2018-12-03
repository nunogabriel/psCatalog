import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPromotions } from 'app/shared/model/promotions.model';

type EntityResponseType = HttpResponse<IPromotions>;
type EntityArrayResponseType = HttpResponse<IPromotions[]>;

@Injectable({ providedIn: 'root' })
export class PromotionsService {
    public resourceUrl = SERVER_API_URL + 'api/promotions';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/promotions';

    constructor(private http: HttpClient) {}

    create(promotions: IPromotions): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(promotions);
        return this.http
            .post<IPromotions>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(promotions: IPromotions): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(promotions);
        return this.http
            .put<IPromotions>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPromotions>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPromotions[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPromotions[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(promotions: IPromotions): IPromotions {
        const copy: IPromotions = Object.assign({}, promotions, {
            promotionStartDate:
                promotions.promotionStartDate != null && promotions.promotionStartDate.isValid()
                    ? promotions.promotionStartDate.toJSON()
                    : null,
            promotionExpiryDate:
                promotions.promotionExpiryDate != null && promotions.promotionExpiryDate.isValid()
                    ? promotions.promotionExpiryDate.toJSON()
                    : null,
            createdDate: promotions.createdDate != null && promotions.createdDate.isValid() ? promotions.createdDate.toJSON() : null,
            lastModifiedDate:
                promotions.lastModifiedDate != null && promotions.lastModifiedDate.isValid() ? promotions.lastModifiedDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.promotionStartDate = res.body.promotionStartDate != null ? moment(res.body.promotionStartDate) : null;
            res.body.promotionExpiryDate = res.body.promotionExpiryDate != null ? moment(res.body.promotionExpiryDate) : null;
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
            res.body.lastModifiedDate = res.body.lastModifiedDate != null ? moment(res.body.lastModifiedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((promotions: IPromotions) => {
                promotions.promotionStartDate = promotions.promotionStartDate != null ? moment(promotions.promotionStartDate) : null;
                promotions.promotionExpiryDate = promotions.promotionExpiryDate != null ? moment(promotions.promotionExpiryDate) : null;
                promotions.createdDate = promotions.createdDate != null ? moment(promotions.createdDate) : null;
                promotions.lastModifiedDate = promotions.lastModifiedDate != null ? moment(promotions.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
