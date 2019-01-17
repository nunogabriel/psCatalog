import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ICart } from 'app/shared/cart/cart.model';

@Component({
    selector: 'jhi-cart-order-det-detail',
    templateUrl: './cart-order-det-detail.component.html'
})
export class CartOrderDetDetailComponent implements OnInit {
    cartOrderDet: ICart;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cartOrderDet }) => {
            this.cartOrderDet = cartOrderDet;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    previousState() {
        window.history.back();
    }
}
