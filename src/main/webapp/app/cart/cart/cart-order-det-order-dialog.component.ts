import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICart } from 'app/shared/cart/cart.model';
import { CartOrderDetService } from './cart-order-det.service';

@Component({
    selector: 'jhi-cart-order-det-order-dialog',
    templateUrl: './cart-order-det-order-dialog.component.html'
})
export class CartOrderDetOrderDialogComponent {
    orderDet: ICart;

    constructor(private cartOrderDetService: CartOrderDetService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmOrder() {
        this.cartOrderDetService.order().subscribe(response => {
            this.eventManager.broadcast({
                name: 'cartOrderDetListModification',
                content: 'Ordered an cartOrderDet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cart-order-det-order-popup',
    template: ''
})
export class CartOrderDetOrderPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cartOrderDet }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CartOrderDetOrderDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.cartOrderDet = cartOrderDet;
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
