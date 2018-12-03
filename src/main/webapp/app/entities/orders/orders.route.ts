import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Orders } from 'app/shared/model/orders.model';
import { OrdersService } from './orders.service';
import { OrdersComponent } from './orders.component';
import { OrdersDetailComponent } from './orders-detail.component';
import { OrdersUpdateComponent } from './orders-update.component';
import { OrdersDeletePopupComponent } from './orders-delete-dialog.component';
import { IOrders } from 'app/shared/model/orders.model';

@Injectable({ providedIn: 'root' })
export class OrdersResolve implements Resolve<IOrders> {
    constructor(private service: OrdersService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Orders> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Orders>) => response.ok),
                map((orders: HttpResponse<Orders>) => orders.body)
            );
        }
        return of(new Orders());
    }
}

export const ordersRoute: Routes = [
    {
        path: 'orders',
        component: OrdersComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders/:id/view',
        component: OrdersDetailComponent,
        resolve: {
            orders: OrdersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders/new',
        component: OrdersUpdateComponent,
        resolve: {
            orders: OrdersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders/:id/edit',
        component: OrdersUpdateComponent,
        resolve: {
            orders: OrdersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ordersPopupRoute: Routes = [
    {
        path: 'orders/:id/delete',
        component: OrdersDeletePopupComponent,
        resolve: {
            orders: OrdersResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
