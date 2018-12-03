import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Countries } from 'app/shared/model/countries.model';
import { CountriesService } from './countries.service';
import { CountriesComponent } from './countries.component';
import { CountriesDetailComponent } from './countries-detail.component';
import { CountriesUpdateComponent } from './countries-update.component';
import { CountriesDeletePopupComponent } from './countries-delete-dialog.component';
import { ICountries } from 'app/shared/model/countries.model';

@Injectable({ providedIn: 'root' })
export class CountriesResolve implements Resolve<ICountries> {
    constructor(private service: CountriesService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Countries> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Countries>) => response.ok),
                map((countries: HttpResponse<Countries>) => countries.body)
            );
        }
        return of(new Countries());
    }
}

export const countriesRoute: Routes = [
    {
        path: 'countries',
        component: CountriesComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.countries.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'countries/:id/view',
        component: CountriesDetailComponent,
        resolve: {
            countries: CountriesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.countries.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'countries/new',
        component: CountriesUpdateComponent,
        resolve: {
            countries: CountriesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.countries.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'countries/:id/edit',
        component: CountriesUpdateComponent,
        resolve: {
            countries: CountriesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.countries.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const countriesPopupRoute: Routes = [
    {
        path: 'countries/:id/delete',
        component: CountriesDeletePopupComponent,
        resolve: {
            countries: CountriesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.countries.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
