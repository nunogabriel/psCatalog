import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrders } from 'app/shared/model/orders.model';

@Component({
    selector: 'jhi-orders-detail',
    templateUrl: './orders-detail.component.html'
})
export class OrdersDetailComponent implements OnInit {
    orders: IOrders;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orders }) => {
            this.orders = orders;
        });
    }

    previousState() {
        window.history.back();
    }
}
