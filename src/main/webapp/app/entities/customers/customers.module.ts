import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    CustomersComponent,
    CustomersDetailComponent,
    CustomersUpdateComponent,
    CustomersDeletePopupComponent,
    CustomersDeleteDialogComponent,
    customersRoute,
    customersPopupRoute
} from './';

const ENTITY_STATES = [...customersRoute, ...customersPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CustomersComponent,
        CustomersDetailComponent,
        CustomersUpdateComponent,
        CustomersDeleteDialogComponent,
        CustomersDeletePopupComponent
    ],
    entryComponents: [CustomersComponent, CustomersUpdateComponent, CustomersDeleteDialogComponent, CustomersDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogCustomersModule {}
