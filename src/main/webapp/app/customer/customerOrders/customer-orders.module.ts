import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    CustomerOrdersComponent,
    CustomerOrdersDetailComponent,
    CustomerOrdersUpdateComponent,
    customerOrdersRoute
} from './';

const ENTITY_STATES = [...customerOrdersRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CustomerOrdersComponent, CustomerOrdersDetailComponent, CustomerOrdersUpdateComponent],
    entryComponents: [CustomerOrdersComponent, CustomerOrdersUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogCustomerOrdersModule {}
