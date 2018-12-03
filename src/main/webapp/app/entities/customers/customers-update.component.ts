import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ICustomers } from 'app/shared/model/customers.model';
import { CustomersService } from './customers.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products';

@Component({
    selector: 'jhi-customers-update',
    templateUrl: './customers-update.component.html'
})
export class CustomersUpdateComponent implements OnInit {
    customers: ICustomers;
    isSaving: boolean;

    products: IProducts[];
    customerBeginDate: string;
    customerEndDate: string;
    createdDate: string;
    lastModifiedDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private customersService: CustomersService,
        private productsService: ProductsService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ customers }) => {
            this.customers = customers;
            this.customerBeginDate =
                this.customers.customerBeginDate != null ? this.customers.customerBeginDate.format(DATE_TIME_FORMAT) : null;
            this.customerEndDate = this.customers.customerEndDate != null ? this.customers.customerEndDate.format(DATE_TIME_FORMAT) : null;
            this.createdDate = this.customers.createdDate != null ? this.customers.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate =
                this.customers.lastModifiedDate != null ? this.customers.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
        });
        this.productsService.query().subscribe(
            (res: HttpResponse<IProducts[]>) => {
                this.products = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.customers.customerBeginDate = this.customerBeginDate != null ? moment(this.customerBeginDate, DATE_TIME_FORMAT) : null;
        this.customers.customerEndDate = this.customerEndDate != null ? moment(this.customerEndDate, DATE_TIME_FORMAT) : null;
        this.customers.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.customers.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;
        if (this.customers.id !== undefined) {
            this.subscribeToSaveResponse(this.customersService.update(this.customers));
        } else {
            this.subscribeToSaveResponse(this.customersService.create(this.customers));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICustomers>>) {
        result.subscribe((res: HttpResponse<ICustomers>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackProductsById(index: number, item: IProducts) {
        return item.id;
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
