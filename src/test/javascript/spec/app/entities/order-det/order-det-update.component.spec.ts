/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { OrderDetUpdateComponent } from 'app/entities/order-det/order-det-update.component';
import { OrderDetService } from 'app/entities/order-det/order-det.service';
import { OrderDet } from 'app/shared/model/order-det.model';

describe('Component Tests', () => {
    describe('OrderDet Management Update Component', () => {
        let comp: OrderDetUpdateComponent;
        let fixture: ComponentFixture<OrderDetUpdateComponent>;
        let service: OrderDetService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrderDetUpdateComponent]
            })
                .overrideTemplate(OrderDetUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrderDetUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrderDetService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new OrderDet(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.orderDet = entity;
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
                    const entity = new OrderDet();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.orderDet = entity;
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
