import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPromotions } from 'app/shared/model/promotions.model';

@Component({
    selector: 'jhi-promotions-detail',
    templateUrl: './promotions-detail.component.html'
})
export class PromotionsDetailComponent implements OnInit {
    promotions: IPromotions;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ promotions }) => {
            this.promotions = promotions;
        });
    }

    previousState() {
        window.history.back();
    }
}
