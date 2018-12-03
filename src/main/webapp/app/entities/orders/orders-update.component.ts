import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IOrders } from 'app/shared/model/orders.model';
import { OrdersService } from './orders.service';
import { IOrderStatus } from 'app/shared/model/order-status.model';
import { OrderStatusService } from 'app/entities/order-status';
import { IAddresses } from 'app/shared/model/addresses.model';
import { AddressesService } from 'app/entities/addresses';
import { ICustomers } from 'app/shared/model/customers.model';
import { CustomersService } from 'app/entities/customers';

@Component({
    selector: 'jhi-orders-update',
    templateUrl: './orders-update.component.html'
})
export class OrdersUpdateComponent implements OnInit {
    orders: IOrders;
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
        private ordersService: OrdersService,
        private orderStatusService: OrderStatusService,
        private addressesService: AddressesService,
        private customersService: CustomersService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orders }) => {
            this.orders = orders;
            this.orderDate = this.orders.orderDate != null ? this.orders.orderDate.format(DATE_TIME_FORMAT) : null;
            this.deliveryDate = this.orders.deliveryDate != null ? this.orders.deliveryDate.format(DATE_TIME_FORMAT) : null;
            this.createdDate = this.orders.createdDate != null ? this.orders.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate = this.orders.lastModifiedDate != null ? this.orders.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
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
        this.orders.orderDate = this.orderDate != null ? moment(this.orderDate, DATE_TIME_FORMAT) : null;
        this.orders.deliveryDate = this.deliveryDate != null ? moment(this.deliveryDate, DATE_TIME_FORMAT) : null;
        this.orders.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.orders.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;
        if (this.orders.id !== undefined) {
            this.subscribeToSaveResponse(this.ordersService.update(this.orders));
        } else {
            this.subscribeToSaveResponse(this.ordersService.create(this.orders));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IOrders>>) {
        result.subscribe((res: HttpResponse<IOrders>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
