import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OrderDetHst } from 'app/shared/model/order-det-hst.model';
import { OrderDetHstService } from './order-det-hst.service';
import { OrderDetHstComponent } from './order-det-hst.component';
import { OrderDetHstDetailComponent } from './order-det-hst-detail.component';
import { OrderDetHstUpdateComponent } from './order-det-hst-update.component';
import { OrderDetHstDeletePopupComponent } from './order-det-hst-delete-dialog.component';
import { IOrderDetHst } from 'app/shared/model/order-det-hst.model';

@Injectable({ providedIn: 'root' })
export class OrderDetHstResolve implements Resolve<IOrderDetHst> {
    constructor(private service: OrderDetHstService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<OrderDetHst> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OrderDetHst>) => response.ok),
                map((orderDetHst: HttpResponse<OrderDetHst>) => orderDetHst.body)
            );
        }
        return of(new OrderDetHst());
    }
}

export const orderDetHstRoute: Routes = [
    {
        path: 'order-det-hst',
        component: OrderDetHstComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.orderDetHst.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'order-det-hst/:id/view',
        component: OrderDetHstDetailComponent,
        resolve: {
            orderDetHst: OrderDetHstResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orderDetHst.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'order-det-hst/new',
        component: OrderDetHstUpdateComponent,
        resolve: {
            orderDetHst: OrderDetHstResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orderDetHst.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'order-det-hst/:id/edit',
        component: OrderDetHstUpdateComponent,
        resolve: {
            orderDetHst: OrderDetHstResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orderDetHst.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const orderDetHstPopupRoute: Routes = [
    {
        path: 'order-det-hst/:id/delete',
        component: OrderDetHstDeletePopupComponent,
        resolve: {
            orderDetHst: OrderDetHstResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.orderDetHst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
