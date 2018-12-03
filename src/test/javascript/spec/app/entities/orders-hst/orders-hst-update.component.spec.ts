/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { OrdersHstUpdateComponent } from 'app/entities/orders-hst/orders-hst-update.component';
import { OrdersHstService } from 'app/entities/orders-hst/orders-hst.service';
import { OrdersHst } from 'app/shared/model/orders-hst.model';

describe('Component Tests', () => {
    describe('OrdersHst Management Update Component', () => {
        let comp: OrdersHstUpdateComponent;
        let fixture: ComponentFixture<OrdersHstUpdateComponent>;
        let service: OrdersHstService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrdersHstUpdateComponent]
            })
                .overrideTemplate(OrdersHstUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrdersHstUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrdersHstService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new OrdersHst(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.ordersHst = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new OrdersHst();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.ordersHst = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
