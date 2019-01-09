import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IPersonalData } from 'app/shared/customer/personal-data.model';
import { PersonalDataService } from './personal-data.service';

@Component({
    selector: 'jhi-personal-data-update',
    templateUrl: './personal-data-update.component.html'
})
export class PersonalDataUpdateComponent implements OnInit {
    personalData: IPersonalData;
    isSaving: boolean;

    constructor(
        private jhiAlertService: JhiAlertService,
        private personalDataService: PersonalDataService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ personalData }) => {
            this.personalData = personalData;

            if ( this.personalData.login == null ) {
               this.personalData.login = 'anonymousUser';
            }
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.personalData.id !== undefined) {
            this.subscribeToSaveResponse(this.personalDataService.update(this.personalData));
        } else {
            this.subscribeToSaveResponse(this.personalDataService.create(this.personalData));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalData>>) {
        result.subscribe((res: HttpResponse<IPersonalData>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
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
