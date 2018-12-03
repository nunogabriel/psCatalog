/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { PromotionsUpdateComponent } from 'app/entities/promotions/promotions-update.component';
import { PromotionsService } from 'app/entities/promotions/promotions.service';
import { Promotions } from 'app/shared/model/promotions.model';

describe('Component Tests', () => {
    describe('Promotions Management Update Component', () => {
        let comp: PromotionsUpdateComponent;
        let fixture: ComponentFixture<PromotionsUpdateComponent>;
        let service: PromotionsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [PromotionsUpdateComponent]
            })
                .overrideTemplate(PromotionsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PromotionsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PromotionsService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Promotions(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.promotions = entity;
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
                    const entity = new Promotions();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.promotions = entity;
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
