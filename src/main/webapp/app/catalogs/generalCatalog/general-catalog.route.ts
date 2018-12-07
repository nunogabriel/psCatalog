import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { GeneralCatalog } from 'app/shared/catalogs/general-catalog.model';
import { GeneralCatalogService } from './general-catalog.service';
import { GeneralCatalogComponent } from './general-catalog.component';
import { GeneralCatalogDetailComponent } from './general-catalog-detail.component';
import { IGeneralCatalog } from 'app/shared/catalogs/general-catalog.model';

@Injectable({ providedIn: 'root' })
export class GeneralCatalogResolve implements Resolve<IGeneralCatalog> {
    constructor(private service: GeneralCatalogService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<GeneralCatalog> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<GeneralCatalog>) => response.ok),
                map((products: HttpResponse<GeneralCatalog>) => products.body)
            );
        }
        return of(new GeneralCatalog());
    }
}

export const generalCatalogRoute: Routes = [
    {
        path: 'generalCatalog',
        component: GeneralCatalogComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'psCatalogApp.generalCatalog.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'generalCatalog/:id/view',
        component: GeneralCatalogDetailComponent,
        resolve: {
            products: GeneralCatalogResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'psCatalogApp.generalCatalog.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
