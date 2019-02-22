import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { NgxSpinnerService } from 'ngx-spinner';

import { IGeneralCatalog } from 'app/shared/catalogs/general-catalog.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE, FIRST_CREATE_CUSTOMER } from 'app/shared';
import { GeneralCatalogService } from './general-catalog.service';

@Component({
    selector: 'jhi-general-catalog',
    templateUrl: './general-catalog.component.html'
})
export class GeneralCatalogComponent implements OnInit, OnDestroy {
    currentAccount: any;
    generalCatalog: IGeneralCatalog[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    firstCreateCustomer: string;

    constructor(
        private generalCatalogService: GeneralCatalogService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager,
        private spinner: NgxSpinnerService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.generalCatalogService
                .search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IGeneralCatalog[]>) => this.paginateGeneralCatalog(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.firstCreateCustomer = null;
        this.generalCatalogService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IGeneralCatalog[]>) => this.paginateGeneralCatalog(res.body, res.headers),
                response => this.onErrorAux(response)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.spinner.show();
            this.previousPage = page;
            this.transition();
            this.spinner.hide();
        }
    }

    transition() {
        this.router.navigate(['/generalCatalog'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.spinner.show();
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            '/generalCatalog',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
        this.spinner.hide();
    }

    search(query) {
        this.spinner.show();
        if (!query) {
            this.spinner.hide();
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            '/generalCatalog',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
        this.spinner.hide();
    }

    ngOnInit() {
        this.spinner.show();
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInGeneralCatalog();
        this.spinner.hide();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IGeneralCatalog) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInGeneralCatalog() {
        this.eventSubscriber = this.eventManager.subscribe('generalCatalogListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateGeneralCatalog(data: IGeneralCatalog[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.generalCatalog = data;
    }

    private onErrorAux(response: HttpErrorResponse) {
        if (response.status === 400 && response.error.type === FIRST_CREATE_CUSTOMER) {
             this.firstCreateCustomer = 'ERROR';
        } else {
             this.jhiAlertService.error(response.message, null, null);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
