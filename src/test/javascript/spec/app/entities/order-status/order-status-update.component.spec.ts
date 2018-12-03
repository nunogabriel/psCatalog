/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { OrderStatusUpdateComponent } from 'app/entities/order-status/order-status-update.component';
import { OrderStatusService } from 'app/entities/order-status/order-status.service';
import { OrderStatus } from 'app/shared/model/order-status.model';

describe('Component Tests', () => {
    describe('OrderStatus Management Update Component', () => {
        let comp: OrderStatusUpdateComponent;
        let fixture: ComponentFixture<OrderStatusUpdateComponent>;
        let service: OrderStatusService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrderStatusUpdateComponent]
            })
                .overrideTemplate(OrderStatusUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrderStatusUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrderStatusService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new OrderStatus(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.orderStatus = entity;
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
                    const entity = new OrderStatus();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.orderStatus = entity;
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
