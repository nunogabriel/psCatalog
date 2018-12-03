import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICountries } from 'app/shared/model/countries.model';

type EntityResponseType = HttpResponse<ICountries>;
type EntityArrayResponseType = HttpResponse<ICountries[]>;

@Injectable({ providedIn: 'root' })
export class CountriesService {
    public resourceUrl = SERVER_API_URL + 'api/countries';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/countries';

    constructor(private http: HttpClient) {}

    create(countries: ICountries): Observable<EntityResponseType> {
        return this.http.post<ICountries>(this.resourceUrl, countries, { observe: 'response' });
    }

    update(countries: ICountries): Observable<EntityResponseType> {
        return this.http.put<ICountries>(this.resourceUrl, countries, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICountries>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICountries[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICountries[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
