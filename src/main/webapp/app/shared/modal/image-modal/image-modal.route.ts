import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Products } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products/products.service';
import { ImageModalPopupComponent } from './image-modal-dialog.component';
import { IProducts } from 'app/shared/model/products.model';

@Injectable({ providedIn: 'root' })
export class ImageModalResolve implements Resolve<IProducts> {
    constructor(private service: ProductsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Products> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Products>) => response.ok),
                map((products: HttpResponse<Products>) => products.body)
            );
        }
        return of(new Products());
    }
}

export const imageModalPopupRoute: Routes = [
    {
        path: 'products/:id/image-modal',
        component: ImageModalPopupComponent,
        resolve: {
            products: ImageModalResolve
        },
        data: {
            authorities: ['ROLE_USER']
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
