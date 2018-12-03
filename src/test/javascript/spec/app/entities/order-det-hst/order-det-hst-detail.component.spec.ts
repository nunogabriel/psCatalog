/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { OrderDetHstDetailComponent } from 'app/entities/order-det-hst/order-det-hst-detail.component';
import { OrderDetHst } from 'app/shared/model/order-det-hst.model';

describe('Component Tests', () => {
    describe('OrderDetHst Management Detail Component', () => {
        let comp: OrderDetHstDetailComponent;
        let fixture: ComponentFixture<OrderDetHstDetailComponent>;
        const route = ({ data: of({ orderDetHst: new OrderDetHst(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrderDetHstDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OrderDetHstDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrderDetHstDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.orderDetHst).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
