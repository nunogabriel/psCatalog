import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerAddresses } from 'app/shared/customer/customer-addresses.model';

@Component({
    selector: 'jhi-customer-addresses-detail',
    templateUrl: './customer-addresses-detail.component.html'
})
export class CustomerAddressesDetailComponent implements OnInit {
    customerAddresses: ICustomerAddresses;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customerAddresses }) => {
            this.customerAddresses = customerAddresses;
        });
    }

    previousState() {
        window.history.back();
    }
}
