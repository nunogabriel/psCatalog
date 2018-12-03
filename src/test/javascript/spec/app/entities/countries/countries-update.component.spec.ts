/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { CountriesUpdateComponent } from 'app/entities/countries/countries-update.component';
import { CountriesService } from 'app/entities/countries/countries.service';
import { Countries } from 'app/shared/model/countries.model';

describe('Component Tests', () => {
    describe('Countries Management Update Component', () => {
        let comp: CountriesUpdateComponent;
        let fixture: ComponentFixture<CountriesUpdateComponent>;
        let service: CountriesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [CountriesUpdateComponent]
            })
                .overrideTemplate(CountriesUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CountriesUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CountriesService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Countries(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.countries = entity;
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
                    const entity = new Countries();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.countries = entity;
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
