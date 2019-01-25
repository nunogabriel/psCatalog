import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CustomerAddresses } from 'app/shared/customer/customer-addresses.model';
import { CustomerAddressesService } from './customer-addresses.service';
import { CustomerAddressesComponent } from './customer-addresses.component';
import { CustomerAddressesDetailComponent } from './customer-addresses-detail.component';
import { CustomerAddressesUpdateComponent } from './customer-addresses-update.component';
import { CustomerAddressesDeletePopupComponent } from './customer-addresses-delete-dialog.component';
import { ICustomerAddresses } from 'app/shared/customer/customer-addresses.model';

@Injectable({ providedIn: 'root' })
export class CustomerAddressesResolve implements Resolve<ICustomerAddresses> {
    constructor(private service: CustomerAddressesService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<CustomerAddresses> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<CustomerAddresses>) => response.ok),
                map((customerAddresses: HttpResponse<CustomerAddresses>) => customerAddresses.body)
            );
        }
        return of(new CustomerAddresses());
    }
}

export const customerAddressesRoute: Routes = [
    {
        path: 'customerAddresses',
        component: CustomerAddressesComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.customerAddresses.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customerAddresses/:id/view',
        component: CustomerAddressesDetailComponent,
        resolve: {
            customerAddresses: CustomerAddressesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customerAddresses.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customerAddresses/new',
        component: CustomerAddressesUpdateComponent,
        resolve: {
            customerAddresses: CustomerAddressesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customerAddresses.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customerAddresses/:id/edit',
        component: CustomerAddressesUpdateComponent,
        resolve: {
            customerAddresses: CustomerAddressesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customerAddresses.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const customerAddressesPopupRoute: Routes = [
    {
        path: 'customerAddresses/:id/delete',
        component: CustomerAddressesDeletePopupComponent,
        resolve: {
            customerAddresses: CustomerAddressesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customerAddresses.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
