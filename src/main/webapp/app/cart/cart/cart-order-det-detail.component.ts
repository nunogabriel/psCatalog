import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICart } from 'app/shared/cart/cart.model';

@Component({
    selector: 'jhi-cart-order-det-detail',
    templateUrl: './cart-order-det-detail.component.html'
})
export class CartOrderDetDetailComponent implements OnInit {
    cartOrderDet: ICart;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cartOrderDet }) => {
            this.cartOrderDet = cartOrderDet;
        });
    }

    previousState() {
        window.history.back();
    }
}
