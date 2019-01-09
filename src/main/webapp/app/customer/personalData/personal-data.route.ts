import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PersonalData } from 'app/shared/customer/personal-data.model';
import { PersonalDataService } from './personal-data.service';
import { PersonalDataComponent } from './personal-data.component';
import { PersonalDataDetailComponent } from './personal-data-detail.component';
import { PersonalDataUpdateComponent } from './personal-data-update.component';
import { PersonalDataDeletePopupComponent } from './personal-data-delete-dialog.component';
import { IPersonalData } from 'app/shared/customer/personal-data.model';

@Injectable({ providedIn: 'root' })
export class PersonalDataResolve implements Resolve<IPersonalData> {
    constructor(private service: PersonalDataService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<PersonalData> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PersonalData>) => response.ok),
                map((personalData: HttpResponse<PersonalData>) => personalData.body)
            );
        }
        return of(new PersonalData());
    }
}

export const personalDataRoute: Routes = [
    {
        path: 'personalData',
        component: PersonalDataComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.personalData.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'personalData/:id/view',
        component: PersonalDataDetailComponent,
        resolve: {
            personalData: PersonalDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.personalData.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'personalData/new',
        component: PersonalDataUpdateComponent,
        resolve: {
            personalData: PersonalDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.personalData.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'personalData/:id/edit',
        component: PersonalDataUpdateComponent,
        resolve: {
            personalData: PersonalDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.personalData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const personalDataPopupRoute: Routes = [
    {
        path: 'personalData/:id/delete',
        component: PersonalDataDeletePopupComponent,
        resolve: {
            personalData: PersonalDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.personalData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
