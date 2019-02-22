import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { NgxSpinnerService } from 'ngx-spinner';

import { IGeneralCatalog } from 'app/shared/catalogs/general-catalog.model';

@Component({
    selector: 'jhi-general-catalog-detail',
    templateUrl: './general-catalog-detail.component.html'
})
export class GeneralCatalogDetailComponent implements OnInit {
    generalCatalog: IGeneralCatalog;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute, private spinner: NgxSpinnerService) {}

    ngOnInit() {
        this.spinner.show();
        this.activatedRoute.data.subscribe(({ generalCatalog }) => {
            this.generalCatalog = generalCatalog;
        });
        this.spinner.hide();
    }

    previousState() {
        this.spinner.show();
        window.history.back();
        this.spinner.hide();
    }
}
