import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OrderDet } from 'app/shared/model/order-det.model';
import { OrderDetService } from './order-det.service';
import { OrderDetComponent } from './order-det.component';
import { OrderDetDetailComponent } from './order-det-detail.component';
import { OrderDetUpdateComponent } from './order-det-update.component';
import { OrderDetDeletePopupComponent } from './order-det-delete-dialog.component';
import { IOrderDet } from 'app/shared/model/order-det.model';

@Injectable({ providedIn: 'root' })
export class OrderDetResolve implements Resolve<IOrderDet> {
    constructor(private service: OrderDetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<OrderDet> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OrderDet>) => response.ok),
                map((orderDet: HttpResponse<OrderDet>) => orderDet.body)
            );
        }
        return of(new OrderDet());
    }
}

export const orderDetRoute: Routes = [
    {
        path: 'order-det',
        component: OrderDetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.orderDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'order-det/:id/view',
        component: OrderDetDetailComponent,
        resolve: {
            orderDet: OrderDetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orderDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'order-det/new',
        component: OrderDetUpdateComponent,
        resolve: {
            orderDet: OrderDetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orderDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'order-det/:id/edit',
        component: OrderDetUpdateComponent,
        resolve: {
            orderDet: OrderDetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orderDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const orderDetPopupRoute: Routes = [
    {
        path: 'order-det/:id/delete',
        component: OrderDetDeletePopupComponent,
        resolve: {
            orderDet: OrderDetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orderDet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
