/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { SuppliersUpdateComponent } from 'app/entities/suppliers/suppliers-update.component';
import { SuppliersService } from 'app/entities/suppliers/suppliers.service';
import { Suppliers } from 'app/shared/model/suppliers.model';

describe('Component Tests', () => {
    describe('Suppliers Management Update Component', () => {
        let comp: SuppliersUpdateComponent;
        let fixture: ComponentFixture<SuppliersUpdateComponent>;
        let service: SuppliersService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [SuppliersUpdateComponent]
            })
                .overrideTemplate(SuppliersUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SuppliersUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SuppliersService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Suppliers(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.suppliers = entity;
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
                    const entity = new Suppliers();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.suppliers = entity;
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
