/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { OrderDetDetailComponent } from 'app/entities/order-det/order-det-detail.component';
import { OrderDet } from 'app/shared/model/order-det.model';

describe('Component Tests', () => {
    describe('OrderDet Management Detail Component', () => {
        let comp: OrderDetDetailComponent;
        let fixture: ComponentFixture<OrderDetDetailComponent>;
        const route = ({ data: of({ orderDet: new OrderDet(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrderDetDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OrderDetDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrderDetDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.orderDet).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
