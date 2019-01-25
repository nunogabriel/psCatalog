import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProducts } from 'app/shared/model/products.model';

@Component({
    selector: 'jhi-image-modal-dialog',
    templateUrl: './image-modal-dialog.component.html',
    styleUrls: ['image-modal.css']
})
export class ImageModalDialogComponent {
    products: IProducts;

    constructor(public activeModal: NgbActiveModal) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }
}

@Component({
    selector: 'jhi-image-modal-popup',
    template: ''
})
export class ImageModalPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ products }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ImageModalDialogComponent as Component, { size: 'sm', backdrop: 'static' });
                this.ngbModalRef.componentInstance.products = products;
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
