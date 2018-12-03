import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    OrdersHstComponent,
    OrdersHstDetailComponent,
    OrdersHstUpdateComponent,
    OrdersHstDeletePopupComponent,
    OrdersHstDeleteDialogComponent,
    ordersHstRoute,
    ordersHstPopupRoute
} from './';

const ENTITY_STATES = [...ordersHstRoute, ...ordersHstPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrdersHstComponent,
        OrdersHstDetailComponent,
        OrdersHstUpdateComponent,
        OrdersHstDeleteDialogComponent,
        OrdersHstDeletePopupComponent
    ],
    entryComponents: [OrdersHstComponent, OrdersHstUpdateComponent, OrdersHstDeleteDialogComponent, OrdersHstDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogOrdersHstModule {}
