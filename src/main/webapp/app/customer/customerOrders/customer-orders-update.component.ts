import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ICustomerOrders } from 'app/shared/customer/customer-orders.model';
import { CustomerOrdersService } from './customer-orders.service';
import { IOrderStatus } from 'app/shared/model/order-status.model';
import { OrderStatusService } from 'app/entities/order-status';
import { IAddresses } from 'app/shared/model/addresses.model';
import { AddressesService } from 'app/entities/addresses';
import { ICustomers } from 'app/shared/model/customers.model';
import { CustomersService } from 'app/entities/customers';

@Component({
    selector: 'jhi-customer-orders-update',
    templateUrl: './customer-orders-update.component.html'
})
export class CustomerOrdersUpdateComponent implements OnInit {
    customerOrders: ICustomerOrders;
    isSaving: boolean;

    orderstatuses: IOrderStatus[];

    addresses: IAddresses[];

    customers: ICustomers[];
    orderDate: string;
    deliveryDate: string;
    createdDate: string;
    lastModifiedDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private customerOrdersService: CustomerOrdersService,
        private orderStatusService: OrderStatusService,
        private addressesService: AddressesService,
        private customersService: CustomersService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ customerOrders }) => {
            this.customerOrders = customerOrders;
            this.orderDate = this.customerOrders.orderDate != null ? this.customerOrders.orderDate.format(DATE_TIME_FORMAT) : null;
            this.deliveryDate = this.customerOrders.deliveryDate != null ? this.customerOrders.deliveryDate.format(DATE_TIME_FORMAT) : null;
            this.createdDate = this.customerOrders.createdDate != null ? this.customerOrders.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate = this.customerOrders.lastModifiedDate != null ? this.customerOrders.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
        });
        this.orderStatusService.query().subscribe(
            (res: HttpResponse<IOrderStatus[]>) => {
                this.orderstatuses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.addressesService.query().subscribe(
            (res: HttpResponse<IAddresses[]>) => {
                this.addresses = res.body;
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

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.customerOrders.orderDate = this.orderDate != null ? moment(this.orderDate, DATE_TIME_FORMAT) : null;
        this.customerOrders.deliveryDate = this.deliveryDate != null ? moment(this.deliveryDate, DATE_TIME_FORMAT) : null;
        this.customerOrders.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.customerOrders.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;

        if (this.customerOrders.id !== undefined) {
            this.subscribeToSaveResponse(this.customerOrdersService.update(this.customerOrders));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerOrders>>) {
        result.subscribe((res: HttpResponse<ICustomerOrders>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackOrderStatusById(index: number, item: IOrderStatus) {
        return item.id;
    }

    trackAddressesById(index: number, item: IAddresses) {
        return item.id;
    }

    trackCustomersById(index: number, item: ICustomers) {
        return item.id;
    }
}
