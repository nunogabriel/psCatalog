import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PersonalCatalog } from 'app/shared/catalogs/personal-catalog.model';
import { PersonalCatalogService } from './personal-catalog.service';
import { PersonalCatalogComponent } from './personal-catalog.component';
import { PersonalCatalogDetailComponent } from './personal-catalog-detail.component';
import { PersonalCatalogAddBasketComponent } from './personal-catalog-add-basket.component';
import { PersonalCatalogDeletePersonalPopupComponent } from './personal-catalog-delete-personal-dialog.component';
import { IPersonalCatalog } from 'app/shared/catalogs/personal-catalog.model';

@Injectable({ providedIn: 'root' })
export class PersonalCatalogResolve implements Resolve<IPersonalCatalog> {
    constructor(private service: PersonalCatalogService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PersonalCatalog> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PersonalCatalog>) => response.ok),
                map((personalCatalog: HttpResponse<PersonalCatalog>) => personalCatalog.body)
            );
        }
        return of(new PersonalCatalog());
    }
}

export const personalCatalogRoute: Routes = [
    {
        path: 'personalCatalog',
        component: PersonalCatalogComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.personalCatalog.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'personalCatalog/:id/view',
        component: PersonalCatalogDetailComponent,
        resolve: {
            personalCatalog: PersonalCatalogResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.personalCatalog.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'personalCatalog/:id/addBasket',
        component: PersonalCatalogAddBasketComponent,
        resolve: {
            personalCatalog: PersonalCatalogResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.personalCatalog.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const personalCatalogPopupRoute: Routes = [
    {
        path: 'personalCatalog/:id/deletePersonal',
        component: PersonalCatalogDeletePersonalPopupComponent,
        resolve: {
            personalCatalog: PersonalCatalogResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.personalCatalog.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
