import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderDet } from 'app/shared/model/order-det.model';

@Component({
    selector: 'jhi-order-det-detail',
    templateUrl: './order-det-detail.component.html'
})
export class OrderDetDetailComponent implements OnInit {
    orderDet: IOrderDet;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orderDet }) => {
            this.orderDet = orderDet;
        });
    }

    previousState() {
        window.history.back();
    }
}
