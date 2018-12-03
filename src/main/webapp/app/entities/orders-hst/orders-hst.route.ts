import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OrdersHst } from 'app/shared/model/orders-hst.model';
import { OrdersHstService } from './orders-hst.service';
import { OrdersHstComponent } from './orders-hst.component';
import { OrdersHstDetailComponent } from './orders-hst-detail.component';
import { OrdersHstUpdateComponent } from './orders-hst-update.component';
import { OrdersHstDeletePopupComponent } from './orders-hst-delete-dialog.component';
import { IOrdersHst } from 'app/shared/model/orders-hst.model';

@Injectable({ providedIn: 'root' })
export class OrdersHstResolve implements Resolve<IOrdersHst> {
    constructor(private service: OrdersHstService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<OrdersHst> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OrdersHst>) => response.ok),
                map((ordersHst: HttpResponse<OrdersHst>) => ordersHst.body)
            );
        }
        return of(new OrdersHst());
    }
}

export const ordersHstRoute: Routes = [
    {
        path: 'orders-hst',
        component: OrdersHstComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.ordersHst.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders-hst/:id/view',
        component: OrdersHstDetailComponent,
        resolve: {
            ordersHst: OrdersHstResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.ordersHst.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders-hst/new',
        component: OrdersHstUpdateComponent,
        resolve: {
            ordersHst: OrdersHstResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.ordersHst.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders-hst/:id/edit',
        component: OrdersHstUpdateComponent,
        resolve: {
            ordersHst: OrdersHstResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.ordersHst.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ordersHstPopupRoute: Routes = [
    {
        path: 'orders-hst/:id/delete',
        component: OrdersHstDeletePopupComponent,
        resolve: {
            ordersHst: OrdersHstResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.ordersHst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
