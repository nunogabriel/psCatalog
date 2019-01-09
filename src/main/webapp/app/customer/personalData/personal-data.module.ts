import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    PersonalDataComponent,
    PersonalDataDetailComponent,
    PersonalDataUpdateComponent,
    PersonalDataDeletePopupComponent,
    PersonalDataDeleteDialogComponent,
    personalDataRoute,
    personalDataPopupRoute
} from './';

const ENTITY_STATES = [...personalDataRoute, ...personalDataPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PersonalDataComponent,
        PersonalDataDetailComponent,
        PersonalDataUpdateComponent,
        PersonalDataDeleteDialogComponent,
        PersonalDataDeletePopupComponent
    ],
    entryComponents: [PersonalDataComponent, PersonalDataUpdateComponent, PersonalDataDeleteDialogComponent, PersonalDataDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogPersonalDataModule {}
