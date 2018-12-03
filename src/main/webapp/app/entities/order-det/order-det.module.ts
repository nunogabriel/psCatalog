import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    OrderDetComponent,
    OrderDetDetailComponent,
    OrderDetUpdateComponent,
    OrderDetDeletePopupComponent,
    OrderDetDeleteDialogComponent,
    orderDetRoute,
    orderDetPopupRoute
} from './';

const ENTITY_STATES = [...orderDetRoute, ...orderDetPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrderDetComponent,
        OrderDetDetailComponent,
        OrderDetUpdateComponent,
        OrderDetDeleteDialogComponent,
        OrderDetDeletePopupComponent
    ],
    entryComponents: [OrderDetComponent, OrderDetUpdateComponent, OrderDetDeleteDialogComponent, OrderDetDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogOrderDetModule {}
