import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { INSUFFICIENT_PRODUCT_QUANTITY } from 'app/shared';

import { ICustomerAddresses } from 'app/shared/customer/customer-addresses.model';
import { CartOrderDetService } from './cart-order-det.service';
import { CustomerAddressesService } from 'app/customer/customerAddresses';
import { CustomerOrdersDetService } from 'app/customer/customerOrdersDet';

@Component({
    selector: 'jhi-cart-order-det-order-dialog',
    templateUrl: './cart-order-det-order-dialog.component.html'
})
export class CartOrderDetOrderDialogComponent {
    insufficientProductQuantity: string;

    constructor(
            private jhiAlertService: JhiAlertService,
            private cartOrderDetService: CartOrderDetService,
            public activeModal: NgbActiveModal,
            private eventManager: JhiEventManager
            ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmOrder(addressId: number, deliveryAddressId: number) {
        this.insufficientProductQuantity = null;
        this.cartOrderDetService.order(addressId, deliveryAddressId).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cartOrderDetListModification',
                content: 'Ordered an cartOrderDet'
            });
            this.activeModal.dismiss(true);
        }, response => this.onSaveError(response));
    }

    private onSaveError(response: HttpErrorResponse) {
        if (response.status === 400 && response.error.type === INSUFFICIENT_PRODUCT_QUANTITY) {
           this.insufficientProductQuantity = 'ERROR';
        } else {
            this.jhiAlertService.error(response.message, null, null);
        }
    }
}

@Component({
    selector: 'jhi-cart-order-det-order-popup',
    template: ''
})
export class CartOrderDetOrderPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;
    customerAddresses: ICustomerAddresses[];
    pendingOrderTotalValue: number;

    constructor(
            private jhiAlertService: JhiAlertService,
            private activatedRoute: ActivatedRoute,
            private router: Router,
            private modalService: NgbModal,
            private customerAddressesService: CustomerAddressesService,
            private customerOrdersDetService: CustomerOrdersDetService
            ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cartOrderDet }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CartOrderDetOrderDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.cartOrderDet = cartOrderDet;
                this.customerOrdersDetService.getPendingOrderTotal().subscribe(
                        (response: HttpResponse<any> ) => this.ngbModalRef.componentInstance.pendingOrderTotalValue = response.body);
                this.customerAddressesService.query().subscribe(
                        (res: HttpResponse<ICustomerAddresses[]>) => {
                            this.ngbModalRef.componentInstance.customerAddresses = res.body;
                        },
                        (res: HttpErrorResponse) => this.ngbModalRef.componentInstance.onError(res.message)
                    );
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

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCustomerAddressesById(index: number, item: ICustomerAddresses) {
        return item.id;
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
