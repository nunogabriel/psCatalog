import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    SuppliersComponent,
    SuppliersDetailComponent,
    SuppliersUpdateComponent,
    SuppliersDeletePopupComponent,
    SuppliersDeleteDialogComponent,
    suppliersRoute,
    suppliersPopupRoute
} from './';

const ENTITY_STATES = [...suppliersRoute, ...suppliersPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SuppliersComponent,
        SuppliersDetailComponent,
        SuppliersUpdateComponent,
        SuppliersDeleteDialogComponent,
        SuppliersDeletePopupComponent
    ],
    entryComponents: [SuppliersComponent, SuppliersUpdateComponent, SuppliersDeleteDialogComponent, SuppliersDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogSuppliersModule {}
