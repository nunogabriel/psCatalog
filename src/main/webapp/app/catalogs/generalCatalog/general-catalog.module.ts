import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    GeneralCatalogComponent,
    GeneralCatalogDetailComponent,
    GeneralCatalogUpdateComponent,
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
        GeneralCatalogUpdateComponent,
        GeneralCatalogAddPersonalDialogComponent,
        GeneralCatalogAddPersonalPopupComponent
    ],
    entryComponents: [GeneralCatalogComponent, GeneralCatalogUpdateComponent, GeneralCatalogAddPersonalDialogComponent, GeneralCatalogAddPersonalPopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogGeneralCatalogModule {}
