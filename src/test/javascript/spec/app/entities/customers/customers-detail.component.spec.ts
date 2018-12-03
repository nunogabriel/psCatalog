/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { CustomersDetailComponent } from 'app/entities/customers/customers-detail.component';
import { Customers } from 'app/shared/model/customers.model';

describe('Component Tests', () => {
    describe('Customers Management Detail Component', () => {
        let comp: CustomersDetailComponent;
        let fixture: ComponentFixture<CustomersDetailComponent>;
        const route = ({ data: of({ customers: new Customers(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [CustomersDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CustomersDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CustomersDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.customers).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
