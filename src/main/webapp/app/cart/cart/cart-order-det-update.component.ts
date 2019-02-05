import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { INSUFFICIENT_PRODUCT_QUANTITY } from 'app/shared';

import { ICart } from 'app/shared/cart/cart.model';
import { CartOrderDetService } from './cart-order-det.service';
import { IOrders } from 'app/shared/model/orders.model';
import { OrdersService } from 'app/entities/orders';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products';

@Component({
    selector: 'jhi-cart-order-det-update',
    templateUrl: './cart-order-det-update.component.html'
})
export class CartOrderDetUpdateComponent implements OnInit {
    cartOrderDet: ICart;
    isSaving: boolean;
    orders: IOrders[];
    products: IProducts[];
    createdDate: string;
    lastModifiedDate: string;
    insufficientProductQuantity: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private cartOrderDetService: CartOrderDetService,
        private ordersService: OrdersService,
        private productsService: ProductsService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cartOrderDet }) => {
            this.cartOrderDet = cartOrderDet;
            this.createdDate = this.cartOrderDet.createdDate != null ? this.cartOrderDet.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate = this.cartOrderDet.lastModifiedDate != null ? this.cartOrderDet.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
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
        this.insufficientProductQuantity = null;
        this.cartOrderDet.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.cartOrderDet.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;
        if (this.cartOrderDet.id !== undefined) {
            this.subscribeToSaveResponse(this.cartOrderDetService.update(this.cartOrderDet));
        } else {
            this.subscribeToSaveResponse(this.cartOrderDetService.create(this.cartOrderDet));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICart>>) {
        result.subscribe((res: HttpResponse<ICart>) => this.onSaveSuccess(), response => this.onSaveError(response));
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError(response: HttpErrorResponse) {
        this.isSaving = false;

        if (response.status === 400 && response.error.type === INSUFFICIENT_PRODUCT_QUANTITY) {
           this.insufficientProductQuantity = 'ERROR';
        } else {
            this.jhiAlertService.error(response.message, null, null);
        }
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
