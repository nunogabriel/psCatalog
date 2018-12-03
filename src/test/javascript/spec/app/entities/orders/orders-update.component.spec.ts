/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { OrdersUpdateComponent } from 'app/entities/orders/orders-update.component';
import { OrdersService } from 'app/entities/orders/orders.service';
import { Orders } from 'app/shared/model/orders.model';

describe('Component Tests', () => {
    describe('Orders Management Update Component', () => {
        let comp: OrdersUpdateComponent;
        let fixture: ComponentFixture<OrdersUpdateComponent>;
        let service: OrdersService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrdersUpdateComponent]
            })
                .overrideTemplate(OrdersUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrdersUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrdersService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Orders(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.orders = entity;
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
                    const entity = new Orders();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.orders = entity;
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
