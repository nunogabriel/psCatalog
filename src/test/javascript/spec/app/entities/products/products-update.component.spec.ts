/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { ProductsUpdateComponent } from 'app/entities/products/products-update.component';
import { ProductsService } from 'app/entities/products/products.service';
import { Products } from 'app/shared/model/products.model';

describe('Component Tests', () => {
    describe('Products Management Update Component', () => {
        let comp: ProductsUpdateComponent;
        let fixture: ComponentFixture<ProductsUpdateComponent>;
        let service: ProductsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [ProductsUpdateComponent]
            })
                .overrideTemplate(ProductsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ProductsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProductsService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Products(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.products = entity;
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
                    const entity = new Products();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.products = entity;
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
