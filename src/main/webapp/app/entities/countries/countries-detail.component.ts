import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountries } from 'app/shared/model/countries.model';

@Component({
    selector: 'jhi-countries-detail',
    templateUrl: './countries-detail.component.html'
})
export class CountriesDetailComponent implements OnInit {
    countries: ICountries;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ countries }) => {
            this.countries = countries;
        });
    }

    previousState() {
        window.history.back();
    }
}
