import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from './products.service';
import { ISuppliers } from 'app/shared/model/suppliers.model';
import { SuppliersService } from 'app/entities/suppliers';
import { ICustomers } from 'app/shared/model/customers.model';
import { CustomersService } from 'app/entities/customers';

@Component({
    selector: 'jhi-products-update',
    templateUrl: './products-update.component.html'
})
export class ProductsUpdateComponent implements OnInit {
    products: IProducts;
    isSaving: boolean;

    suppliers: ISuppliers[];

    customers: ICustomers[];
    productStartDate: string;
    productEndDate: string;
    createdDate: string;
    lastModifiedDate: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private productsService: ProductsService,
        private suppliersService: SuppliersService,
        private customersService: CustomersService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ products }) => {
            this.products = products;
            this.productStartDate = this.products.productStartDate != null ? this.products.productStartDate.format(DATE_TIME_FORMAT) : null;
            this.productEndDate = this.products.productEndDate != null ? this.products.productEndDate.format(DATE_TIME_FORMAT) : null;
            this.createdDate = this.products.createdDate != null ? this.products.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate = this.products.lastModifiedDate != null ? this.products.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
        });
        this.suppliersService.query().subscribe(
            (res: HttpResponse<ISuppliers[]>) => {
                this.suppliers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.customersService.query().subscribe(
            (res: HttpResponse<ICustomers[]>) => {
                this.customers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
        this.dataUtils.clearInputImage(this.products, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.products.productStartDate = this.productStartDate != null ? moment(this.productStartDate, DATE_TIME_FORMAT) : null;
        this.products.productEndDate = this.productEndDate != null ? moment(this.productEndDate, DATE_TIME_FORMAT) : null;
        this.products.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.products.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;
        if (this.products.id !== undefined) {
            this.subscribeToSaveResponse(this.productsService.update(this.products));
        } else {
            this.subscribeToSaveResponse(this.productsService.create(this.products));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProducts>>) {
        result.subscribe((res: HttpResponse<IProducts>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSuppliersById(index: number, item: ISuppliers) {
        return item.id;
    }

    trackCustomersById(index: number, item: ICustomers) {
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
