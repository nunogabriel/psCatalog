import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IOrdersHst } from 'app/shared/model/orders-hst.model';
import { OrdersHstService } from './orders-hst.service';
import { IOrders } from 'app/shared/model/orders.model';
import { OrdersService } from 'app/entities/orders';

@Component({
    selector: 'jhi-orders-hst-update',
    templateUrl: './orders-hst-update.component.html'
})
export class OrdersHstUpdateComponent implements OnInit {
    ordersHst: IOrdersHst;
    isSaving: boolean;

    orders: IOrders[];
    orderDate: string;
    deliveryDate: string;
    createdDate: string;
    lastModifiedDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private ordersHstService: OrdersHstService,
        private ordersService: OrdersService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ ordersHst }) => {
            this.ordersHst = ordersHst;
            this.orderDate = this.ordersHst.orderDate != null ? this.ordersHst.orderDate.format(DATE_TIME_FORMAT) : null;
            this.deliveryDate = this.ordersHst.deliveryDate != null ? this.ordersHst.deliveryDate.format(DATE_TIME_FORMAT) : null;
            this.createdDate = this.ordersHst.createdDate != null ? this.ordersHst.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate =
                this.ordersHst.lastModifiedDate != null ? this.ordersHst.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
        });
        this.ordersService.query({ filter: 'ordershst-is-null' }).subscribe(
            (res: HttpResponse<IOrders[]>) => {
                if (!this.ordersHst.ordersId) {
                    this.orders = res.body;
                } else {
                    this.ordersService.find(this.ordersHst.ordersId).subscribe(
                        (subRes: HttpResponse<IOrders>) => {
                            this.orders = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.ordersHst.orderDate = this.orderDate != null ? moment(this.orderDate, DATE_TIME_FORMAT) : null;
        this.ordersHst.deliveryDate = this.deliveryDate != null ? moment(this.deliveryDate, DATE_TIME_FORMAT) : null;
        this.ordersHst.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.ordersHst.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;
        if (this.ordersHst.id !== undefined) {
            this.subscribeToSaveResponse(this.ordersHstService.update(this.ordersHst));
        } else {
            this.subscribeToSaveResponse(this.ordersHstService.create(this.ordersHst));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IOrdersHst>>) {
        result.subscribe((res: HttpResponse<IOrdersHst>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackOrdersById(index: number, item: IOrders) {
        return item.id;
    }
}
