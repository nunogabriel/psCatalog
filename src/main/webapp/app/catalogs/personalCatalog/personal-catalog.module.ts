import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    PersonalCatalogComponent,
    PersonalCatalogDetailComponent,
    PersonalCatalogAddBasketComponent,
    PersonalCatalogDeletePersonalPopupComponent,
    PersonalCatalogDeletePersonalDialogComponent,
    personalCatalogRoute,
    personalCatalogPopupRoute
} from './';

const ENTITY_STATES = [...personalCatalogRoute, ...personalCatalogPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PersonalCatalogComponent,
        PersonalCatalogDetailComponent,
        PersonalCatalogAddBasketComponent,
        PersonalCatalogDeletePersonalDialogComponent,
        PersonalCatalogDeletePersonalPopupComponent
    ],
    entryComponents: [PersonalCatalogComponent, PersonalCatalogAddBasketComponent, PersonalCatalogDeletePersonalDialogComponent, PersonalCatalogDeletePersonalPopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogPersonalCatalogModule {}
