import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrdersHst } from 'app/shared/model/orders-hst.model';
import { OrdersHstService } from './orders-hst.service';

@Component({
    selector: 'jhi-orders-hst-delete-dialog',
    templateUrl: './orders-hst-delete-dialog.component.html'
})
export class OrdersHstDeleteDialogComponent {
    ordersHst: IOrdersHst;

    constructor(private ordersHstService: OrdersHstService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ordersHstService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ordersHstListModification',
                content: 'Deleted an ordersHst'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-orders-hst-delete-popup',
    template: ''
})
export class OrdersHstDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ ordersHst }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OrdersHstDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.ordersHst = ordersHst;
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
