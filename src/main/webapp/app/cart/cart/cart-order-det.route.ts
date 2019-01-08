import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Cart } from 'app/shared/cart/cart.model';
import { CartOrderDetService } from './cart-order-det.service';
import { CartOrderDetComponent } from './cart-order-det.component';
import { CartOrderDetDetailComponent } from './cart-order-det-detail.component';
import { CartOrderDetUpdateComponent } from './cart-order-det-update.component';
import { CartOrderDetDeletePopupComponent } from './cart-order-det-delete-dialog.component';
import { CartOrderDetOrderPopupComponent } from './cart-order-det-order-dialog.component';
import { ICart } from 'app/shared/cart/cart.model';

@Injectable({ providedIn: 'root' })
export class CartOrderDetResolve implements Resolve<ICart> {
    constructor(private service: CartOrderDetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Cart> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Cart>) => response.ok),
                map((orderDet: HttpResponse<Cart>) => orderDet.body)
            );
        }
        return of(new Cart());
    }
}

export const cartOrderDetRoute: Routes = [
    {
        path: 'cart-order-det',
        component: CartOrderDetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.cartOrderDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cart-order-det/:id/view',
        component: CartOrderDetDetailComponent,
        resolve: {
            cartOrderDet: CartOrderDetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.cartOrderDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cart-order-det/new',
        component: CartOrderDetUpdateComponent,
        resolve: {
            cartOrderDet: CartOrderDetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.cartOrderDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cart-order-det/:id/edit',
        component: CartOrderDetUpdateComponent,
        resolve: {
            cartOrderDet: CartOrderDetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.cartOrderDet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cartOrderDetPopupRoute: Routes = [
    {
        path: 'cart-order-det/order',
        component: CartOrderDetOrderPopupComponent,
        resolve: {
            cartOrderDet: CartOrderDetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.cartOrderDet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cart-order-det/:id/delete',
        component: CartOrderDetDeletePopupComponent,
        resolve: {
            cartOrderDet: CartOrderDetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.cartOrderDet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
