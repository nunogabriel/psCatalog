import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ISuppliers } from 'app/shared/model/suppliers.model';
import { SuppliersService } from './suppliers.service';

@Component({
    selector: 'jhi-suppliers-update',
    templateUrl: './suppliers-update.component.html'
})
export class SuppliersUpdateComponent implements OnInit {
    suppliers: ISuppliers;
    isSaving: boolean;
    supplierBeginDate: string;
    supplierEndDate: string;
    createdDate: string;
    lastModifiedDate: string;

    constructor(private suppliersService: SuppliersService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ suppliers }) => {
            this.suppliers = suppliers;
            this.supplierBeginDate =
                this.suppliers.supplierBeginDate != null ? this.suppliers.supplierBeginDate.format(DATE_TIME_FORMAT) : null;
            this.supplierEndDate = this.suppliers.supplierEndDate != null ? this.suppliers.supplierEndDate.format(DATE_TIME_FORMAT) : null;
            this.createdDate = this.suppliers.createdDate != null ? this.suppliers.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate =
                this.suppliers.lastModifiedDate != null ? this.suppliers.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.suppliers.supplierBeginDate = this.supplierBeginDate != null ? moment(this.supplierBeginDate, DATE_TIME_FORMAT) : null;
        this.suppliers.supplierEndDate = this.supplierEndDate != null ? moment(this.supplierEndDate, DATE_TIME_FORMAT) : null;
        this.suppliers.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.suppliers.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;
        if (this.suppliers.id !== undefined) {
            this.subscribeToSaveResponse(this.suppliersService.update(this.suppliers));
        } else {
            this.subscribeToSaveResponse(this.suppliersService.create(this.suppliers));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISuppliers>>) {
        result.subscribe((res: HttpResponse<ISuppliers>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
