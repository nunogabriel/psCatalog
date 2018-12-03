import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISuppliers } from 'app/shared/model/suppliers.model';

@Component({
    selector: 'jhi-suppliers-detail',
    templateUrl: './suppliers-detail.component.html'
})
export class SuppliersDetailComponent implements OnInit {
    suppliers: ISuppliers;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ suppliers }) => {
            this.suppliers = suppliers;
        });
    }

    previousState() {
        window.history.back();
    }
}
