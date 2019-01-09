import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    CustomerAddressesComponent,
    CustomerAddressesDetailComponent,
    CustomerAddressesUpdateComponent,
    CustomerAddressesDeletePopupComponent,
    CustomerAddressesDeleteDialogComponent,
    customerAddressesRoute,
    customerAddressesPopupRoute
} from './';

const ENTITY_STATES = [...customerAddressesRoute, ...customerAddressesPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CustomerAddressesComponent,
        CustomerAddressesDetailComponent,
        CustomerAddressesUpdateComponent,
        CustomerAddressesDeleteDialogComponent,
        CustomerAddressesDeletePopupComponent
    ],
    entryComponents: [CustomerAddressesComponent, CustomerAddressesUpdateComponent, CustomerAddressesDeleteDialogComponent, CustomerAddressesDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogCustomerAddressesModule {}
