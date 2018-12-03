/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ProductsService } from 'app/entities/products/products.service';
import { IProducts, Products, ProductTypeEnum } from 'app/shared/model/products.model';

describe('Service Tests', () => {
    describe('Products Service', () => {
        let injector: TestBed;
        let service: ProductsService;
        let httpMock: HttpTestingController;
        let elemDefault: IProducts;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ProductsService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Products(
                0,
                'AAAAAAA',
                'AAAAAAA',
                0,
                currentDate,
                currentDate,
                ProductTypeEnum.PRODUTO,
                'image/png',
                'AAAAAAA',
                'AAAAAAA',
                currentDate,
                'AAAAAAA',
                currentDate
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        productStartDate: currentDate.format(DATE_TIME_FORMAT),
                        productEndDate: currentDate.format(DATE_TIME_FORMAT),
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Products', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        productStartDate: currentDate.format(DATE_TIME_FORMAT),
                        productEndDate: currentDate.format(DATE_TIME_FORMAT),
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        productStartDate: currentDate,
                        productEndDate: currentDate,
                        createdDate: currentDate,
                        lastModifiedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Products(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Products', async () => {
                const returnedFromService = Object.assign(
                    {
                        productName: 'BBBBBB',
                        productDescription: 'BBBBBB',
                        productPrice: 1,
                        productStartDate: currentDate.format(DATE_TIME_FORMAT),
                        productEndDate: currentDate.format(DATE_TIME_FORMAT),
                        productType: 'BBBBBB',
                        productImg: 'BBBBBB',
                        createdBy: 'BBBBBB',
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        lastModifiedBy: 'BBBBBB',
                        lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        productStartDate: currentDate,
                        productEndDate: currentDate,
                        createdDate: currentDate,
                        lastModifiedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Products', async () => {
                const returnedFromService = Object.assign(
                    {
                        productName: 'BBBBBB',
                        productDescription: 'BBBBBB',
                        productPrice: 1,
                        productStartDate: currentDate.format(DATE_TIME_FORMAT),
                        productEndDate: currentDate.format(DATE_TIME_FORMAT),
                        productType: 'BBBBBB',
                        productImg: 'BBBBBB',
                        createdBy: 'BBBBBB',
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        lastModifiedBy: 'BBBBBB',
                        lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        productStartDate: currentDate,
                        productEndDate: currentDate,
                        createdDate: currentDate,
                        lastModifiedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Products', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
