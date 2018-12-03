import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    OrderStatusComponent,
    OrderStatusDetailComponent,
    OrderStatusUpdateComponent,
    OrderStatusDeletePopupComponent,
    OrderStatusDeleteDialogComponent,
    orderStatusRoute,
    orderStatusPopupRoute
} from './';

const ENTITY_STATES = [...orderStatusRoute, ...orderStatusPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrderStatusComponent,
        OrderStatusDetailComponent,
        OrderStatusUpdateComponent,
        OrderStatusDeleteDialogComponent,
        OrderStatusDeletePopupComponent
    ],
    entryComponents: [OrderStatusComponent, OrderStatusUpdateComponent, OrderStatusDeleteDialogComponent, OrderStatusDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogOrderStatusModule {}
