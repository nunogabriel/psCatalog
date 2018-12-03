import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IPromotions } from 'app/shared/model/promotions.model';
import { PromotionsService } from './promotions.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products';

@Component({
    selector: 'jhi-promotions-update',
    templateUrl: './promotions-update.component.html'
})
export class PromotionsUpdateComponent implements OnInit {
    promotions: IPromotions;
    isSaving: boolean;

    products: IProducts[];
    promotionStartDate: string;
    promotionExpiryDate: string;
    createdDate: string;
    lastModifiedDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private promotionsService: PromotionsService,
        private productsService: ProductsService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ promotions }) => {
            this.promotions = promotions;
            this.promotionStartDate =
                this.promotions.promotionStartDate != null ? this.promotions.promotionStartDate.format(DATE_TIME_FORMAT) : null;
            this.promotionExpiryDate =
                this.promotions.promotionExpiryDate != null ? this.promotions.promotionExpiryDate.format(DATE_TIME_FORMAT) : null;
            this.createdDate = this.promotions.createdDate != null ? this.promotions.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate =
                this.promotions.lastModifiedDate != null ? this.promotions.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
        });
        this.productsService.query({ filter: 'promotions-is-null' }).subscribe(
            (res: HttpResponse<IProducts[]>) => {
                if (!this.promotions.productsId) {
                    this.products = res.body;
                } else {
                    this.productsService.find(this.promotions.productsId).subscribe(
                        (subRes: HttpResponse<IProducts>) => {
                            this.products = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.promotions.promotionStartDate = this.promotionStartDate != null ? moment(this.promotionStartDate, DATE_TIME_FORMAT) : null;
        this.promotions.promotionExpiryDate = this.promotionExpiryDate != null ? moment(this.promotionExpiryDate, DATE_TIME_FORMAT) : null;
        this.promotions.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.promotions.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;
        if (this.promotions.id !== undefined) {
            this.subscribeToSaveResponse(this.promotionsService.update(this.promotions));
        } else {
            this.subscribeToSaveResponse(this.promotionsService.create(this.promotions));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPromotions>>) {
        result.subscribe((res: HttpResponse<IPromotions>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackProductsById(index: number, item: IProducts) {
        return item.id;
    }
}
