import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { FIRST_CREATE_ADDRESS, INSUFFICIENT_PRODUCT_QUANTITY } from 'app/shared';
import { IPersonalCatalog } from 'app/shared/catalogs/personal-catalog.model';
import { PersonalCatalogService } from './personal-catalog.service';

@Component({
    selector: 'jhi-personal-catalog-add-basket',
    templateUrl: './personal-catalog-add-basket.component.html'
})
export class PersonalCatalogAddBasketComponent implements OnInit {
    personalCatalog: IPersonalCatalog;
    isSaving: boolean;
    firstCreateAddress: string;
    insufficientProductQuantity: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private personalCatalogService: PersonalCatalogService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ personalCatalog }) => {
            this.personalCatalog = personalCatalog;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.personalCatalog, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.firstCreateAddress = null;
        this.insufficientProductQuantity = null;
        if (this.personalCatalog.id !== undefined) {
            this.subscribeToSaveResponse(this.personalCatalogService.addBasket(this.personalCatalog));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalCatalog>>) {
        result.subscribe((res: HttpResponse<IPersonalCatalog>) => this.onSaveSuccess(), response => this.onSaveError(response));
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError(response: HttpErrorResponse) {
        this.isSaving = false;

        if (response.status === 400 && response.error.type === FIRST_CREATE_ADDRESS) {
            this.firstCreateAddress = 'ERROR';
        } else if (response.status === 400 && response.error.type === INSUFFICIENT_PRODUCT_QUANTITY) {
           this.insufficientProductQuantity = 'ERROR';
        } else {
            this.jhiAlertService.error(response.message, null, null);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
