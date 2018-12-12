import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IGeneralCatalog } from 'app/shared/catalogs/general-catalog.model';

@Component({
    selector: 'jhi-general-catalog-detail',
    templateUrl: './general-catalog-detail.component.html'
})
export class GeneralCatalogDetailComponent implements OnInit {
    generalCatalog: IGeneralCatalog;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ generalCatalog }) => {
            this.generalCatalog = generalCatalog;
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
