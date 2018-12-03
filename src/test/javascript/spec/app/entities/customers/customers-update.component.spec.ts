/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { CustomersUpdateComponent } from 'app/entities/customers/customers-update.component';
import { CustomersService } from 'app/entities/customers/customers.service';
import { Customers } from 'app/shared/model/customers.model';

describe('Component Tests', () => {
    describe('Customers Management Update Component', () => {
        let comp: CustomersUpdateComponent;
        let fixture: ComponentFixture<CustomersUpdateComponent>;
        let service: CustomersService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [CustomersUpdateComponent]
            })
                .overrideTemplate(CustomersUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CustomersUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CustomersService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Customers(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.customers = entity;
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
                    const entity = new Customers();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.customers = entity;
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
