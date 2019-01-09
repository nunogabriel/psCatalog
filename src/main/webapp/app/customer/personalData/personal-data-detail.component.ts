import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonalData } from 'app/shared/customer/personal-data.model';

@Component({
    selector: 'jhi-personal-data-detail',
    templateUrl: './personal-data-detail.component.html'
})
export class PersonalDataDetailComponent implements OnInit {
    personalData: IPersonalData;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ personalData }) => {
            this.personalData = personalData;
        });
    }

    previousState() {
        window.history.back();
    }
}
