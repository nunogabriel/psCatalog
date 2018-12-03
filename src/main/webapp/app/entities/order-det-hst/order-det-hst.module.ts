import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    OrderDetHstComponent,
    OrderDetHstDetailComponent,
    OrderDetHstUpdateComponent,
    OrderDetHstDeletePopupComponent,
    OrderDetHstDeleteDialogComponent,
    orderDetHstRoute,
    orderDetHstPopupRoute
} from './';

const ENTITY_STATES = [...orderDetHstRoute, ...orderDetHstPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrderDetHstComponent,
        OrderDetHstDetailComponent,
        OrderDetHstUpdateComponent,
        OrderDetHstDeleteDialogComponent,
        OrderDetHstDeletePopupComponent
    ],
    entryComponents: [OrderDetHstComponent, OrderDetHstUpdateComponent, OrderDetHstDeleteDialogComponent, OrderDetHstDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogOrderDetHstModule {}
