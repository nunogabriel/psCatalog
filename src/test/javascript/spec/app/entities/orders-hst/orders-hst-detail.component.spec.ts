/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { OrdersHstDetailComponent } from 'app/entities/orders-hst/orders-hst-detail.component';
import { OrdersHst } from 'app/shared/model/orders-hst.model';

describe('Component Tests', () => {
    describe('OrdersHst Management Detail Component', () => {
        let comp: OrdersHstDetailComponent;
        let fixture: ComponentFixture<OrdersHstDetailComponent>;
        const route = ({ data: of({ ordersHst: new OrdersHst(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrdersHstDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OrdersHstDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrdersHstDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.ordersHst).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
