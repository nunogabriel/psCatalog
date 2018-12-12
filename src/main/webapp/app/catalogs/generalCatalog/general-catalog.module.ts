import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    GeneralCatalogComponent,
    GeneralCatalogDetailComponent,
    GeneralCatalogUpdateComponent,
    GeneralCatalogDeletePopupComponent,
    GeneralCatalogDeleteDialogComponent,
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
        GeneralCatalogDeleteDialogComponent,
        GeneralCatalogDeletePopupComponent
    ],
    entryComponents: [GeneralCatalogComponent, GeneralCatalogUpdateComponent, GeneralCatalogDeleteDialogComponent, GeneralCatalogDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogGeneralCatalogModule {}
