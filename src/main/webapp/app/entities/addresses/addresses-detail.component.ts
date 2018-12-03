import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAddresses } from 'app/shared/model/addresses.model';

@Component({
    selector: 'jhi-addresses-detail',
    templateUrl: './addresses-detail.component.html'
})
export class AddressesDetailComponent implements OnInit {
    addresses: IAddresses;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ addresses }) => {
            this.addresses = addresses;
        });
    }

    previousState() {
        window.history.back();
    }
}
