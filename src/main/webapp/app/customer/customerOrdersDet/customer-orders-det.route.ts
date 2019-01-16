import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CustomerOrdersDet } from 'app/shared/customer/customer-orders-det.model';
import { CustomerOrdersDetService } from './customer-orders-det.service';
import { CustomerOrdersDetComponent } from './customer-orders-det.component';
import { CustomerOrdersDetDetailComponent } from './customer-orders-det-detail.component';
import { ICustomerOrdersDet } from 'app/shared/customer/customer-orders-det.model';

@Injectable({ providedIn: 'root' })
export class CustomerOrdersDetResolve implements Resolve<ICustomerOrdersDet> {
    constructor(private service: CustomerOrdersDetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<CustomerOrdersDet> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<CustomerOrdersDet>) => response.ok),
                map((orderDet: HttpResponse<CustomerOrdersDet>) => orderDet.body)
            );
        }
        return of(new CustomerOrdersDet());
    }
}

export const customerOrdersDetRoute: Routes = [
    {
        path: 'customer-orders-det',
        component: CustomerOrdersDetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.customerOrdersDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customer-orders-det/:id/view',
        component: CustomerOrdersDetDetailComponent,
        resolve: {
            customerOrdersDet: CustomerOrdersDetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customerOrdersDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customer-orders-det/:orderId/detail',
        component: CustomerOrdersDetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.customerOrdersDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
