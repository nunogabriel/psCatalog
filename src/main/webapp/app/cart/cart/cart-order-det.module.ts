import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    CartOrderDetComponent,
    CartOrderDetDetailComponent,
    CartOrderDetUpdateComponent,
    CartOrderDetDeletePopupComponent,
    CartOrderDetDeleteDialogComponent,
    CartOrderDetOrderPopupComponent, CartOrderDetOrderDialogComponent, cartOrderDetRoute,
    cartOrderDetPopupRoute
} from './';

const ENTITY_STATES = [...cartOrderDetRoute, ...cartOrderDetPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CartOrderDetComponent,
        CartOrderDetDetailComponent,
        CartOrderDetUpdateComponent,
        CartOrderDetDeleteDialogComponent,
        CartOrderDetDeletePopupComponent,
        CartOrderDetOrderDialogComponent,
        CartOrderDetOrderPopupComponent
    ],
    entryComponents: [CartOrderDetComponent,
                      CartOrderDetUpdateComponent,
                      CartOrderDetDeleteDialogComponent,
                      CartOrderDetDeletePopupComponent, CartOrderDetOrderDialogComponent, CartOrderDetOrderPopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogCartOrderDetModule {}
