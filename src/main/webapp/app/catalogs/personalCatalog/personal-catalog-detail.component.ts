import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IPersonalCatalog } from 'app/shared/catalogs/personal-catalog.model';

@Component({
    selector: 'jhi-personal-catalog-detail',
    templateUrl: './personal-catalog-detail.component.html'
})
export class PersonalCatalogDetailComponent implements OnInit {
    personalCatalog: IPersonalCatalog;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ personalCatalog }) => {
            this.personalCatalog = personalCatalog;
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
