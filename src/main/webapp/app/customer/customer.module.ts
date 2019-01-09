import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PsCatalogPersonalDataModule } from './personalData/personal-data.module';
import { PsCatalogCustomerAddressesModule } from './customerAddresses/customer-addresses.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        PsCatalogPersonalDataModule,
        PsCatalogCustomerAddressesModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogCustomerModule {}
