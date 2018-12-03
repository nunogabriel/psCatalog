import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomers } from 'app/shared/model/customers.model';

@Component({
    selector: 'jhi-customers-detail',
    templateUrl: './customers-detail.component.html'
})
export class CustomersDetailComponent implements OnInit {
    customers: ICustomers;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customers }) => {
            this.customers = customers;
        });
    }

    previousState() {
        window.history.back();
    }
}
