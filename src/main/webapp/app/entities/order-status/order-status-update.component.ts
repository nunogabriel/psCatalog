import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IOrderStatus } from 'app/shared/model/order-status.model';
import { OrderStatusService } from './order-status.service';

@Component({
    selector: 'jhi-order-status-update',
    templateUrl: './order-status-update.component.html'
})
export class OrderStatusUpdateComponent implements OnInit {
    orderStatus: IOrderStatus;
    isSaving: boolean;

    constructor(private orderStatusService: OrderStatusService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orderStatus }) => {
            this.orderStatus = orderStatus;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.orderStatus.id !== undefined) {
            this.subscribeToSaveResponse(this.orderStatusService.update(this.orderStatus));
        } else {
            this.subscribeToSaveResponse(this.orderStatusService.create(this.orderStatus));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IOrderStatus>>) {
        result.subscribe((res: HttpResponse<IOrderStatus>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
