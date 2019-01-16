import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerOrders } from 'app/shared/customer/customer-orders.model';

@Component({
    selector: 'jhi-customer-orders-detail',
    templateUrl: './customer-orders-detail.component.html'
})
export class CustomerOrdersDetailComponent implements OnInit {
    customerOrders: ICustomerOrders;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customerOrders }) => {
            this.customerOrders = customerOrders;
        });
    }

    previousState() {
        window.history.back();
    }
}
