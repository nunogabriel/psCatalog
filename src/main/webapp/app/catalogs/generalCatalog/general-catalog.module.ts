import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    GeneralCatalogComponent,
    GeneralCatalogDetailComponent,
    generalCatalogRoute
} from './';

const ENTITY_STATES = [...generalCatalogRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GeneralCatalogComponent,
        GeneralCatalogDetailComponent
    ],
    entryComponents: [GeneralCatalogComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogGeneralCatalogModule {}
