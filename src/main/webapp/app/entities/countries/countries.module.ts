import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    CountriesComponent,
    CountriesDetailComponent,
    CountriesUpdateComponent,
    CountriesDeletePopupComponent,
    CountriesDeleteDialogComponent,
    countriesRoute,
    countriesPopupRoute
} from './';

const ENTITY_STATES = [...countriesRoute, ...countriesPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CountriesComponent,
        CountriesDetailComponent,
        CountriesUpdateComponent,
        CountriesDeleteDialogComponent,
        CountriesDeletePopupComponent
    ],
    entryComponents: [CountriesComponent, CountriesUpdateComponent, CountriesDeleteDialogComponent, CountriesDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogCountriesModule {}
