/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { OrderDetHstUpdateComponent } from 'app/entities/order-det-hst/order-det-hst-update.component';
import { OrderDetHstService } from 'app/entities/order-det-hst/order-det-hst.service';
import { OrderDetHst } from 'app/shared/model/order-det-hst.model';

describe('Component Tests', () => {
    describe('OrderDetHst Management Update Component', () => {
        let comp: OrderDetHstUpdateComponent;
        let fixture: ComponentFixture<OrderDetHstUpdateComponent>;
        let service: OrderDetHstService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrderDetHstUpdateComponent]
            })
                .overrideTemplate(OrderDetHstUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrderDetHstUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrderDetHstService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new OrderDetHst(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.orderDetHst = entity;
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
                    const entity = new OrderDetHst();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.orderDetHst = entity;
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
