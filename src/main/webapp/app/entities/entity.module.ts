import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PsCatalogSuppliersModule } from './suppliers/suppliers.module';
import { PsCatalogCustomersModule } from './customers/customers.module';
import { PsCatalogProductsModule } from './products/products.module';
import { PsCatalogPromotionsModule } from './promotions/promotions.module';
import { PsCatalogAddressesModule } from './addresses/addresses.module';
import { PsCatalogCountriesModule } from './countries/countries.module';
import { PsCatalogOrdersModule } from './orders/orders.module';
import { PsCatalogOrdersHstModule } from './orders-hst/orders-hst.module';
import { PsCatalogOrderStatusModule } from './order-status/order-status.module';
import { PsCatalogOrderDetModule } from './order-det/order-det.module';
import { PsCatalogOrderDetHstModule } from './order-det-hst/order-det-hst.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        PsCatalogSuppliersModule,
        PsCatalogCustomersModule,
        PsCatalogProductsModule,
        PsCatalogPromotionsModule,
        PsCatalogAddressesModule,
        PsCatalogCountriesModule,
        PsCatalogOrdersModule,
        PsCatalogOrdersHstModule,
        PsCatalogOrderStatusModule,
        PsCatalogOrderDetModule,
        PsCatalogOrderDetHstModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogEntityModule {}
