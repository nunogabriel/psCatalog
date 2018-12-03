import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPromotions } from 'app/shared/model/promotions.model';
import { PromotionsService } from './promotions.service';

@Component({
    selector: 'jhi-promotions-delete-dialog',
    templateUrl: './promotions-delete-dialog.component.html'
})
export class PromotionsDeleteDialogComponent {
    promotions: IPromotions;

    constructor(private promotionsService: PromotionsService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.promotionsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'promotionsListModification',
                content: 'Deleted an promotions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-promotions-delete-popup',
    template: ''
})
export class PromotionsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ promotions }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PromotionsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.promotions = promotions;
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
