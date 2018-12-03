import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    AddressesComponent,
    AddressesDetailComponent,
    AddressesUpdateComponent,
    AddressesDeletePopupComponent,
    AddressesDeleteDialogComponent,
    addressesRoute,
    addressesPopupRoute
} from './';

const ENTITY_STATES = [...addressesRoute, ...addressesPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AddressesComponent,
        AddressesDetailComponent,
        AddressesUpdateComponent,
        AddressesDeleteDialogComponent,
        AddressesDeletePopupComponent
    ],
    entryComponents: [AddressesComponent, AddressesUpdateComponent, AddressesDeleteDialogComponent, AddressesDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogAddressesModule {}
