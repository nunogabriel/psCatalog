import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPersonalData } from 'app/shared/customer/personal-data.model';
import { PersonalDataService } from './personal-data.service';

@Component({
    selector: 'jhi-personal-data-delete-dialog',
    templateUrl: './personal-data-delete-dialog.component.html'
})
export class PersonalDataDeleteDialogComponent {
    customers: IPersonalData;

    constructor(private personalDataService: PersonalDataService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.personalDataService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'personalDataListModification',
                content: 'Deleted an personal data'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-personal-data-delete-popup',
    template: ''
})
export class PersonalDataDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ personalData }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PersonalDataDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.personalData = personalData;
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
