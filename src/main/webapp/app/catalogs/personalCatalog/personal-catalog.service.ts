import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPersonalCatalog } from 'app/shared/catalogs/personal-catalog.model';

type CatalogResponseType = HttpResponse<IPersonalCatalog>;
type CatalogArrayResponseType = HttpResponse<IPersonalCatalog[]>;

@Injectable({ providedIn: 'root' })
export class PersonalCatalogService {
    public resourceUrl = SERVER_API_URL + 'api/personalCatalog';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/personalCatalog';

    constructor(private http: HttpClient) {}

    addBasket(personalCatalog: IPersonalCatalog): Observable<CatalogResponseType> {
        return this.http.put<IPersonalCatalog>(this.resourceUrl, personalCatalog, { observe: 'response' });
    }

    find(id: number): Observable<CatalogResponseType> {
        return this.http
            .get<IPersonalCatalog>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<CatalogArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPersonalCatalog[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    deletePersonal(id: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/${id}/deletePersonal`, { observe: 'response' });
    }

    search(req?: any): Observable<CatalogArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPersonalCatalog[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
