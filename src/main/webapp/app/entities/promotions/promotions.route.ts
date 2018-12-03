import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Promotions } from 'app/shared/model/promotions.model';
import { PromotionsService } from './promotions.service';
import { PromotionsComponent } from './promotions.component';
import { PromotionsDetailComponent } from './promotions-detail.component';
import { PromotionsUpdateComponent } from './promotions-update.component';
import { PromotionsDeletePopupComponent } from './promotions-delete-dialog.component';
import { IPromotions } from 'app/shared/model/promotions.model';

@Injectable({ providedIn: 'root' })
export class PromotionsResolve implements Resolve<IPromotions> {
    constructor(private service: PromotionsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Promotions> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Promotions>) => response.ok),
                map((promotions: HttpResponse<Promotions>) => promotions.body)
            );
        }
        return of(new Promotions());
    }
}

export const promotionsRoute: Routes = [
    {
        path: 'promotions',
        component: PromotionsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.promotions.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promotions/:id/view',
        component: PromotionsDetailComponent,
        resolve: {
            promotions: PromotionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.promotions.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promotions/new',
        component: PromotionsUpdateComponent,
        resolve: {
            promotions: PromotionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.promotions.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promotions/:id/edit',
        component: PromotionsUpdateComponent,
        resolve: {
            promotions: PromotionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.promotions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const promotionsPopupRoute: Routes = [
    {
        path: 'promotions/:id/delete',
        component: PromotionsDeletePopupComponent,
        resolve: {
            promotions: PromotionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.promotions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
