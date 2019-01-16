import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CustomerOrders } from 'app/shared/customer/customer-orders.model';
import { CustomerOrdersService } from './customer-orders.service';
import { CustomerOrdersComponent } from './customer-orders.component';
import { CustomerOrdersDetailComponent } from './customer-orders-detail.component';
import { CustomerOrdersUpdateComponent } from './customer-orders-update.component';
import { ICustomerOrders } from 'app/shared/customer/customer-orders.model';

@Injectable({ providedIn: 'root' })
export class CustomerOrdersResolve implements Resolve<ICustomerOrders> {
    constructor(private service: CustomerOrdersService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<CustomerOrders> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<CustomerOrders>) => response.ok),
                map((orders: HttpResponse<CustomerOrders>) => orders.body)
            );
        }
        return of(new CustomerOrders());
    }
}

export const customerOrdersRoute: Routes = [
    {
        path: 'customer-orders',
        component: CustomerOrdersComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.customerOrders.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customer-orders/:id/view',
        component: CustomerOrdersDetailComponent,
        resolve: {
            customerOrders: CustomerOrdersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customerOrders.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customer-orders/:id/edit',
        component: CustomerOrdersUpdateComponent,
        resolve: {
            customerOrders: CustomerOrdersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.customerOrders.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
