import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGeneralCatalog } from 'app/shared/catalogs/general-catalog.model';
import { GeneralCatalogService } from './general-catalog.service';

import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'jhi-general-catalog-add-personal-dialog',
    templateUrl: './general-catalog-add-personal-dialog.component.html'
})
export class GeneralCatalogAddPersonalDialogComponent {
    generalCatalog: IGeneralCatalog;

    constructor(private generalCatalogService: GeneralCatalogService, public activeModal: NgbActiveModal,
            private eventManager: JhiEventManager, private spinner: NgxSpinnerService) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmAddPersonal(id: number) {
        this.generalCatalogService.addPersonal(id).subscribe(response => {
            this.spinner.show();
            this.eventManager.broadcast({
                name: 'generalCatalogListModification',
                content: 'Add Personal an general catalog'
            });
            this.spinner.hide();
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-general-catalog-add-personal-popup',
    template: ''
})
export class GeneralCatalogAddPersonalPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal, private spinner: NgxSpinnerService) {}

    ngOnInit() {
        this.spinner.show();
        this.activatedRoute.data.subscribe(({ generalCatalog }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GeneralCatalogAddPersonalDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.generalCatalog = generalCatalog;
                this.spinner.hide();
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
        this.spinner.hide();
    }
}
