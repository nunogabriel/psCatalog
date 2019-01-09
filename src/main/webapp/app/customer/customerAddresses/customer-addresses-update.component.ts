import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ICustomerAddresses } from 'app/shared/customer/customer-addresses.model';
import { CustomerAddressesService } from './customer-addresses.service';
import { ICustomers } from 'app/shared/model/customers.model';
import { CustomersService } from 'app/entities/customers';
import { ISuppliers } from 'app/shared/model/suppliers.model';
import { SuppliersService } from 'app/entities/suppliers';
import { ICountries } from 'app/shared/model/countries.model';
import { CountriesService } from 'app/entities/countries';

@Component({
    selector: 'jhi-customer-addresses-update',
    templateUrl: './customer-addresses-update.component.html'
})
export class CustomerAddressesUpdateComponent implements OnInit {
    customerAddresses: ICustomerAddresses;
    isSaving: boolean;

    customers: ICustomers[];

    suppliers: ISuppliers[];

    countries: ICountries[];
    createdDate: string;
    lastModifiedDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private customerAddressesService: CustomerAddressesService,
        private customersService: CustomersService,
        private suppliersService: SuppliersService,
        private countriesService: CountriesService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ customerAddresses }) => {
            this.customerAddresses = customerAddresses;
            this.createdDate = this.customerAddresses.createdDate != null ? this.customerAddresses.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate =
                this.customerAddresses.lastModifiedDate != null ? this.customerAddresses.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
        });
        this.customersService.query().subscribe(
            (res: HttpResponse<ICustomers[]>) => {
                this.customers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.suppliersService.query().subscribe(
            (res: HttpResponse<ISuppliers[]>) => {
                this.suppliers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.countriesService.query().subscribe(
            (res: HttpResponse<ICountries[]>) => {
                this.countries = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.customerAddresses.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.customerAddresses.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;

        if (this.customerAddresses.id !== undefined) {
            this.subscribeToSaveResponse(this.customerAddressesService.update(this.customerAddresses));
        } else {
            this.subscribeToSaveResponse(this.customerAddressesService.create(this.customerAddresses));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerAddresses>>) {
        result.subscribe((res: HttpResponse<ICustomerAddresses>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCustomersById(index: number, item: ICustomers) {
        return item.id;
    }

    trackSuppliersById(index: number, item: ISuppliers) {
        return item.id;
    }

    trackCountriesById(index: number, item: ICountries) {
        return item.id;
    }
}
