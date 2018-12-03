import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IOrderDet } from 'app/shared/model/order-det.model';
import { OrderDetService } from './order-det.service';
import { IOrders } from 'app/shared/model/orders.model';
import { OrdersService } from 'app/entities/orders';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products';

@Component({
    selector: 'jhi-order-det-update',
    templateUrl: './order-det-update.component.html'
})
export class OrderDetUpdateComponent implements OnInit {
    orderDet: IOrderDet;
    isSaving: boolean;

    orders: IOrders[];

    products: IProducts[];
    createdDate: string;
    lastModifiedDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private orderDetService: OrderDetService,
        private ordersService: OrdersService,
        private productsService: ProductsService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orderDet }) => {
            this.orderDet = orderDet;
            this.createdDate = this.orderDet.createdDate != null ? this.orderDet.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate = this.orderDet.lastModifiedDate != null ? this.orderDet.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
        });
        this.ordersService.query().subscribe(
            (res: HttpResponse<IOrders[]>) => {
                this.orders = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
        this.orderDet.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.orderDet.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;
        if (this.orderDet.id !== undefined) {
            this.subscribeToSaveResponse(this.orderDetService.update(this.orderDet));
        } else {
            this.subscribeToSaveResponse(this.orderDetService.create(this.orderDet));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IOrderDet>>) {
        result.subscribe((res: HttpResponse<IOrderDet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackProductsById(index: number, item: IProducts) {
        return item.id;
    }
}
