import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGeneralCatalog } from 'app/shared/catalogs/general-catalog.model';
import { GeneralCatalogService } from './general-catalog.service';

@Component({
    selector: 'jhi-general-catalog-delete-dialog',
    templateUrl: './general-catalog-delete-dialog.component.html'
})
export class GeneralCatalogDeleteDialogComponent {
    generalCatalog: IGeneralCatalog;

    constructor(private generalCatalogService: GeneralCatalogService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.generalCatalogService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'generalCatalogListModification',
                content: 'Deleted an general catalog'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-general-catalog-delete-popup',
    template: ''
})
export class GeneralCatalogDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ generalCatalog }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GeneralCatalogDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.generalCatalog = generalCatalog;
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
    }
}
