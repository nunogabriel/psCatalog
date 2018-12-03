import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrdersHst } from 'app/shared/model/orders-hst.model';

@Component({
    selector: 'jhi-orders-hst-detail',
    templateUrl: './orders-hst-detail.component.html'
})
export class OrdersHstDetailComponent implements OnInit {
    ordersHst: IOrdersHst;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ ordersHst }) => {
            this.ordersHst = ordersHst;
        });
    }

    previousState() {
        window.history.back();
    }
}
