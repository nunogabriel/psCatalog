import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Addresses } from 'app/shared/model/addresses.model';
import { AddressesService } from './addresses.service';
import { AddressesComponent } from './addresses.component';
import { AddressesDetailComponent } from './addresses-detail.component';
import { AddressesUpdateComponent } from './addresses-update.component';
import { AddressesDeletePopupComponent } from './addresses-delete-dialog.component';
import { IAddresses } from 'app/shared/model/addresses.model';

@Injectable({ providedIn: 'root' })
export class AddressesResolve implements Resolve<IAddresses> {
    constructor(private service: AddressesService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Addresses> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Addresses>) => response.ok),
                map((addresses: HttpResponse<Addresses>) => addresses.body)
            );
        }
        return of(new Addresses());
    }
}

export const addressesRoute: Routes = [
    {
        path: 'addresses',
        component: AddressesComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.addresses.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'addresses/:id/view',
        component: AddressesDetailComponent,
        resolve: {
            addresses: AddressesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.addresses.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'addresses/new',
        component: AddressesUpdateComponent,
        resolve: {
            addresses: AddressesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.addresses.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'addresses/:id/edit',
        component: AddressesUpdateComponent,
        resolve: {
            addresses: AddressesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.addresses.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const addressesPopupRoute: Routes = [
    {
        path: 'addresses/:id/delete',
        component: AddressesDeletePopupComponent,
        resolve: {
            addresses: AddressesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.addresses.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
