import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomerAddresses } from 'app/shared/customer/customer-addresses.model';
import { CustomerAddressesService } from './customer-addresses.service';

@Component({
    selector: 'jhi-customer-addresses-delete-dialog',
    templateUrl: './customer-addresses-delete-dialog.component.html'
})
export class CustomerAddressesDeleteDialogComponent {
    customerAddresses: ICustomerAddresses;

    constructor(private customerAddressesService: CustomerAddressesService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.customerAddressesService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'customerAddressesListModification',
                content: 'Deleted an customer addresses'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-customer-addresses-delete-popup',
    template: ''
})
export class CustomerAddressesDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customerAddresses }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CustomerAddressesDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.customerAddresses = customerAddresses;
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
