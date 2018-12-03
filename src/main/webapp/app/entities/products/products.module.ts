import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    ProductsComponent,
    ProductsDetailComponent,
    ProductsUpdateComponent,
    ProductsDeletePopupComponent,
    ProductsDeleteDialogComponent,
    productsRoute,
    productsPopupRoute
} from './';

const ENTITY_STATES = [...productsRoute, ...productsPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProductsComponent,
        ProductsDetailComponent,
        ProductsUpdateComponent,
        ProductsDeleteDialogComponent,
        ProductsDeletePopupComponent
    ],
    entryComponents: [ProductsComponent, ProductsUpdateComponent, ProductsDeleteDialogComponent, ProductsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogProductsModule {}
