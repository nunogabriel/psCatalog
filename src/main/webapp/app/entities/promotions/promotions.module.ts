import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsCatalogSharedModule } from 'app/shared';
import {
    PromotionsComponent,
    PromotionsDetailComponent,
    PromotionsUpdateComponent,
    PromotionsDeletePopupComponent,
    PromotionsDeleteDialogComponent,
    promotionsRoute,
    promotionsPopupRoute
} from './';

const ENTITY_STATES = [...promotionsRoute, ...promotionsPopupRoute];

@NgModule({
    imports: [PsCatalogSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PromotionsComponent,
        PromotionsDetailComponent,
        PromotionsUpdateComponent,
        PromotionsDeleteDialogComponent,
        PromotionsDeletePopupComponent
    ],
    entryComponents: [PromotionsComponent, PromotionsUpdateComponent, PromotionsDeleteDialogComponent, PromotionsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PsCatalogPromotionsModule {}
