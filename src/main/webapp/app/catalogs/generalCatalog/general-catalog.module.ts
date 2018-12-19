import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    GeneralCatalogComponent,
    GeneralCatalogDetailComponent,
    GeneralCatalogAddBasketComponent,
    GeneralCatalogAddPersonalPopupComponent,
    GeneralCatalogAddPersonalDialogComponent,
    generalCatalogRoute,
    generalCatalogPopupRoute
} from './';

const ENTITY_STATES = [...generalCatalogRoute, ...generalCatalogPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GeneralCatalogComponent,
        GeneralCatalogDetailComponent,
        GeneralCatalogAddBasketComponent,
        GeneralCatalogAddPersonalDialogComponent,
        GeneralCatalogAddPersonalPopupComponent
    ],
    entryComponents: [GeneralCatalogComponent, GeneralCatalogAddBasketComponent, GeneralCatalogAddPersonalDialogComponent, GeneralCatalogAddPersonalPopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogGeneralCatalogModule {}
