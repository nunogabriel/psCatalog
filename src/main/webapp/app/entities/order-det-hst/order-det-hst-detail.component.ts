import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderDetHst } from 'app/shared/model/order-det-hst.model';

@Component({
    selector: 'jhi-order-det-hst-detail',
    templateUrl: './order-det-hst-detail.component.html'
})
export class OrderDetHstDetailComponent implements OnInit {
    orderDetHst: IOrderDetHst;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orderDetHst }) => {
            this.orderDetHst = orderDetHst;
        });
    }

    previousState() {
        window.history.back();
    }
}
