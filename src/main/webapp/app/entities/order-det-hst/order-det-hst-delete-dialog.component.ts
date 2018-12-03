import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrderDetHst } from 'app/shared/model/order-det-hst.model';
import { OrderDetHstService } from './order-det-hst.service';

@Component({
    selector: 'jhi-order-det-hst-delete-dialog',
    templateUrl: './order-det-hst-delete-dialog.component.html'
})
export class OrderDetHstDeleteDialogComponent {
    orderDetHst: IOrderDetHst;

    constructor(
        private orderDetHstService: OrderDetHstService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.orderDetHstService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'orderDetHstListModification',
                content: 'Deleted an orderDetHst'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-order-det-hst-delete-popup',
    template: ''
})
export class OrderDetHstDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orderDetHst }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OrderDetHstDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.orderDetHst = orderDetHst;
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
