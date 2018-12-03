/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { CustomersService } from 'app/entities/customers/customers.service';
import { ICustomers, Customers, GenderEnum } from 'app/shared/model/customers.model';

describe('Service Tests', () => {
    describe('Customers Service', () => {
        let injector: TestBed;
        let service: CustomersService;
        let httpMock: HttpTestingController;
        let elemDefault: ICustomers;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(CustomersService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Customers(
                0,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                GenderEnum.MASCULINO,
                currentDate,
                currentDate,
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
                        customerBeginDate: currentDate.format(DATE_TIME_FORMAT),
                        customerEndDate: currentDate.format(DATE_TIME_FORMAT),
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

            it('should create a Customers', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        customerBeginDate: currentDate.format(DATE_TIME_FORMAT),
                        customerEndDate: currentDate.format(DATE_TIME_FORMAT),
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        customerBeginDate: currentDate,
                        customerEndDate: currentDate,
                        createdDate: currentDate,
                        lastModifiedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Customers(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Customers', async () => {
                const returnedFromService = Object.assign(
                    {
                        customerName: 'BBBBBB',
                        customerEmail: 'BBBBBB',
                        customerNif: 'BBBBBB',
                        customerPhone: 'BBBBBB',
                        customerGender: 'BBBBBB',
                        customerBeginDate: currentDate.format(DATE_TIME_FORMAT),
                        customerEndDate: currentDate.format(DATE_TIME_FORMAT),
                        createdBy: 'BBBBBB',
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        lastModifiedBy: 'BBBBBB',
                        lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        customerBeginDate: currentDate,
                        customerEndDate: currentDate,
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

            it('should return a list of Customers', async () => {
                const returnedFromService = Object.assign(
                    {
                        customerName: 'BBBBBB',
                        customerEmail: 'BBBBBB',
                        customerNif: 'BBBBBB',
                        customerPhone: 'BBBBBB',
                        customerGender: 'BBBBBB',
                        customerBeginDate: currentDate.format(DATE_TIME_FORMAT),
                        customerEndDate: currentDate.format(DATE_TIME_FORMAT),
                        createdBy: 'BBBBBB',
                        createdDate: currentDate.format(DATE_TIME_FORMAT),
                        lastModifiedBy: 'BBBBBB',
                        lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        customerBeginDate: currentDate,
                        customerEndDate: currentDate,
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

            it('should delete a Customers', async () => {
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
