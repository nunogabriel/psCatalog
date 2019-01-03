import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPersonalCatalog } from 'app/shared/catalogs/personal-catalog.model';
import { PersonalCatalogService } from './personal-catalog.service';

@Component({
    selector: 'jhi-personal-catalog-delete-personal-dialog',
    templateUrl: './personal-catalog-delete-personal-dialog.component.html'
})
export class PersonalCatalogDeletePersonalDialogComponent {
    personalCatalog: IPersonalCatalog;

    constructor(private personalCatalogService: PersonalCatalogService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDeletePersonal(id: number) {
        this.personalCatalogService.deletePersonal(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'personalCatalogListModification',
                content: 'Delete Personal an personal catalog'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-personal-catalog-delete-personal-popup',
    template: ''
})
export class PersonalCatalogDeletePersonalPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ personalCatalog }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PersonalCatalogDeletePersonalDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.personalCatalog = personalCatalog;
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
