import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderStatus } from 'app/shared/model/order-status.model';

@Component({
    selector: 'jhi-order-status-detail',
    templateUrl: './order-status-detail.component.html'
})
export class OrderStatusDetailComponent implements OnInit {
    orderStatus: IOrderStatus;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orderStatus }) => {
            this.orderStatus = orderStatus;
        });
    }

    previousState() {
        window.history.back();
    }
}
