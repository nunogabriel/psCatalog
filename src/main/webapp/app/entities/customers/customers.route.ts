import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Customers } from 'app/shared/model/customers.model';
import { CustomersService } from './customers.service';
import { CustomersComponent } from './customers.component';
import { CustomersDetailComponent } from './customers-detail.component';
import { CustomersUpdateComponent } from './customers-update.component';
import { CustomersDeletePopupComponent } from './customers-delete-dialog.component';
import { ICustomers } from 'app/shared/model/customers.model';

@Injectable({ providedIn: 'root' })
export class CustomersResolve implements Resolve<ICustomers> {
    constructor(private service: CustomersService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Customers> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Customers>) => response.ok),
                map((customers: HttpResponse<Customers>) => customers.body)
            );
        }
        return of(new Customers());
    }
}

export const customersRoute: Routes = [
    {
        path: 'customers',
        component: CustomersComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.customers.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customers/:id/view',
        component: CustomersDetailComponent,
        resolve: {
            customers: CustomersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customers.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customers/new',
        component: CustomersUpdateComponent,
        resolve: {
            customers: CustomersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customers.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customers/:id/edit',
        component: CustomersUpdateComponent,
        resolve: {
            customers: CustomersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customers.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const customersPopupRoute: Routes = [
    {
        path: 'customers/:id/delete',
        component: CustomersDeletePopupComponent,
        resolve: {
            customers: CustomersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
