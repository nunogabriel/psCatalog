import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGeneralCatalog } from 'app/shared/catalogs/general-catalog.model';

type CatalogResponseType = HttpResponse<IGeneralCatalog>;
type CatalogArrayResponseType = HttpResponse<IGeneralCatalog[]>;

@Injectable({ providedIn: 'root' })
export class GeneralCatalogService {
    public resourceUrl = SERVER_API_URL + 'api/generalCatalog';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/generalCatalog';

    constructor(private http: HttpClient) {}

    addBasket(generalCatalog: IGeneralCatalog): Observable<CatalogResponseType> {
        return this.http.put<IGeneralCatalog>(this.resourceUrl, generalCatalog, { observe: 'response' });
    }

    find(id: number): Observable<CatalogResponseType> {
        return this.http
            .get<IGeneralCatalog>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<CatalogArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IGeneralCatalog[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    addPersonal(id: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/${id}/addPersonal`, { observe: 'response' });
    }

    search(req?: any): Observable<CatalogArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IGeneralCatalog[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
