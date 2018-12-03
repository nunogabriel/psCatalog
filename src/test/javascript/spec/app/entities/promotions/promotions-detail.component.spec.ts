/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PsCatalogTestModule } from '../../../test.module';
import { PromotionsDetailComponent } from 'app/entities/promotions/promotions-detail.component';
import { Promotions } from 'app/shared/model/promotions.model';

describe('Component Tests', () => {
    describe('Promotions Management Detail Component', () => {
        let comp: PromotionsDetailComponent;
        let fixture: ComponentFixture<PromotionsDetailComponent>;
        const route = ({ data: of({ promotions: new Promotions(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [PromotionsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PromotionsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PromotionsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.promotions).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
