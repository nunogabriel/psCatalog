import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IGeneralCatalog } from 'app/shared/catalogs/general-catalog.model';

@Component({
    selector: 'jhi-products-detail',
    templateUrl: './general-catalog-detail.component.html'
})
export class GeneralCatalogDetailComponent implements OnInit {
    products: IGeneralCatalog;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ products }) => {
            this.products = products;
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
