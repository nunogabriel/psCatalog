import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    CustomerOrdersDetComponent,
    CustomerOrdersDetDetailComponent,
    customerOrdersDetRoute
} from './';

const ENTITY_STATES = [...customerOrdersDetRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CustomerOrdersDetComponent,
        CustomerOrdersDetDetailComponent
    ],
    entryComponents: [CustomerOrdersDetComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogCustomerOrdersDetModule {}
