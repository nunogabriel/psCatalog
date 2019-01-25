import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    ImageModalPopupComponent,
    ImageModalDialogComponent,
    imageModalPopupRoute
} from './';

const ENTITY_STATES = [...imageModalPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ImageModalDialogComponent,
        ImageModalPopupComponent
    ],
    entryComponents: [ImageModalDialogComponent, ImageModalPopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogImageModalModule {}
