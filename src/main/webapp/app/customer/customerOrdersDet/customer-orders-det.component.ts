import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ICustomerOrdersDet } from 'app/shared/customer/customer-orders-det.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE, FIRST_CREATE_CUSTOMER } from 'app/shared';
import { CustomerOrdersDetService } from './customer-orders-det.service';

@Component({
    selector: 'jhi-customer-orders-det',
    templateUrl: './customer-orders-det.component.html'
})
export class CustomerOrdersDetComponent implements OnInit, OnDestroy {
    currentAccount: any;
    customerOrdersDet: ICustomerOrdersDet[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    currentOrderId: number;
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
    orderTotalValue: number;

    constructor(
        private customerOrdersDetService: CustomerOrdersDetService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager
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
        this.currentOrderId =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['orderId']
                ? this.activatedRoute.snapshot.params['orderId']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.customerOrdersDetService
                .search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<ICustomerOrdersDet[]>) => this.paginateCustomerOrdersDets(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        if (this.currentOrderId) {
            this.customerOrdersDetService
                .queryByOrderId({
                    page: this.page - 1,
                    orderId: this.currentOrderId,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<ICustomerOrdersDet[]>) => this.paginateCustomerOrdersDets(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            this.customerOrdersDetService.getOrderTotalByOrderId(this.currentOrderId).subscribe((response: HttpResponse<any> ) => this.orderTotalValue = response.body);
            return;
        }
        this.firstCreateCustomer = null;
        this.customerOrdersDetService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<ICustomerOrdersDet[]>) => this.paginateCustomerOrdersDets(res.body, res.headers),
                response => this.onErrorAux(response)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/customer-orders-det'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                orderId: this.currentOrderId,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            '/customer-orders-det/' + this.currentOrderId + '/detail',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            '/customer-orders-det',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCustomerOrdersDets();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICustomerOrdersDet) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInCustomerOrdersDets() {
        this.eventSubscriber = this.eventManager.subscribe('customerOrdersDetListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    previousState() {
        window.history.back();
    }

    private paginateCustomerOrdersDets(data: ICustomerOrdersDet[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.customerOrdersDet = data;
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
