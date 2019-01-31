import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IAddresses } from 'app/shared/model/addresses.model';
import { AddressesService } from './addresses.service';
import { ICustomers } from 'app/shared/model/customers.model';
import { CustomersService } from 'app/entities/customers';
import { ISuppliers } from 'app/shared/model/suppliers.model';
import { SuppliersService } from 'app/entities/suppliers';
import { ICountries } from 'app/shared/model/countries.model';
import { CountriesService } from 'app/entities/countries';

@Component({
    selector: 'jhi-addresses-update',
    templateUrl: './addresses-update.component.html'
})
export class AddressesUpdateComponent implements OnInit {
    addresses: IAddresses;
    isSaving: boolean;

    customers: ICustomers[];

    suppliers: ISuppliers[];

    countries: ICountries[];
    createdDate: string;
    lastModifiedDate: string;
    addressBeginDate: string;
    addressEndDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private addressesService: AddressesService,
        private customersService: CustomersService,
        private suppliersService: SuppliersService,
        private countriesService: CountriesService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ addresses }) => {
            this.addresses = addresses;
            this.createdDate = this.addresses.createdDate != null ? this.addresses.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate =
                this.addresses.lastModifiedDate != null ? this.addresses.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
            this.addressBeginDate =
                this.addresses.addressBeginDate != null ? this.addresses.addressBeginDate.format(DATE_TIME_FORMAT) : null;
            this.addressEndDate = this.addresses.addressEndDate != null ? this.addresses.addressEndDate.format(DATE_TIME_FORMAT) : null;
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
        this.addresses.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.addresses.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;
        this.addresses.addressBeginDate = this.addressBeginDate != null ? moment(this.addressBeginDate, DATE_TIME_FORMAT) : null;
        this.addresses.addressEndDate = this.addressEndDate != null ? moment(this.addressEndDate, DATE_TIME_FORMAT) : null;
        if (this.addresses.id !== undefined) {
            this.subscribeToSaveResponse(this.addressesService.update(this.addresses));
        } else {
            this.subscribeToSaveResponse(this.addressesService.create(this.addresses));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAddresses>>) {
        result.subscribe((res: HttpResponse<IAddresses>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
