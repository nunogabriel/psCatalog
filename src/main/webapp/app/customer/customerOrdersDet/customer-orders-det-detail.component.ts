import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ICustomerOrdersDet } from 'app/shared/customer/customer-orders-det.model';

@Component({
    selector: 'jhi-customer-orders-det-detail',
    templateUrl: './customer-orders-det-detail.component.html'
})
export class CustomerOrdersDetDetailComponent implements OnInit {
    customerOrdersDet: ICustomerOrdersDet;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customerOrdersDet }) => {
            this.customerOrdersDet = customerOrdersDet;
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
