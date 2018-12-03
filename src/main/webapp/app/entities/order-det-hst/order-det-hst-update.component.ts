import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IOrderDetHst } from 'app/shared/model/order-det-hst.model';
import { OrderDetHstService } from './order-det-hst.service';
import { IOrderDet } from 'app/shared/model/order-det.model';
import { OrderDetService } from 'app/entities/order-det';

@Component({
    selector: 'jhi-order-det-hst-update',
    templateUrl: './order-det-hst-update.component.html'
})
export class OrderDetHstUpdateComponent implements OnInit {
    orderDetHst: IOrderDetHst;
    isSaving: boolean;

    orderdets: IOrderDet[];
    createdDate: string;
    lastModifiedDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private orderDetHstService: OrderDetHstService,
        private orderDetService: OrderDetService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orderDetHst }) => {
            this.orderDetHst = orderDetHst;
            this.createdDate = this.orderDetHst.createdDate != null ? this.orderDetHst.createdDate.format(DATE_TIME_FORMAT) : null;
            this.lastModifiedDate =
                this.orderDetHst.lastModifiedDate != null ? this.orderDetHst.lastModifiedDate.format(DATE_TIME_FORMAT) : null;
        });
        this.orderDetService.query({ filter: 'orderdethst-is-null' }).subscribe(
            (res: HttpResponse<IOrderDet[]>) => {
                if (!this.orderDetHst.orderDetId) {
                    this.orderdets = res.body;
                } else {
                    this.orderDetService.find(this.orderDetHst.orderDetId).subscribe(
                        (subRes: HttpResponse<IOrderDet>) => {
                            this.orderdets = [subRes.body].concat(res.body);
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
        this.orderDetHst.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        this.orderDetHst.lastModifiedDate = this.lastModifiedDate != null ? moment(this.lastModifiedDate, DATE_TIME_FORMAT) : null;
        if (this.orderDetHst.id !== undefined) {
            this.subscribeToSaveResponse(this.orderDetHstService.update(this.orderDetHst));
        } else {
            this.subscribeToSaveResponse(this.orderDetHstService.create(this.orderDetHst));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IOrderDetHst>>) {
        result.subscribe((res: HttpResponse<IOrderDetHst>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackOrderDetById(index: number, item: IOrderDet) {
        return item.id;
    }
}
