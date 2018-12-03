/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsCatalogTestModule } from '../../../test.module';
import { SuppliersDeleteDialogComponent } from 'app/entities/suppliers/suppliers-delete-dialog.component';
import { SuppliersService } from 'app/entities/suppliers/suppliers.service';

describe('Component Tests', () => {
    describe('Suppliers Management Delete Component', () => {
        let comp: SuppliersDeleteDialogComponent;
        let fixture: ComponentFixture<SuppliersDeleteDialogComponent>;
        let service: SuppliersService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [SuppliersDeleteDialogComponent]
            })
                .overrideTemplate(SuppliersDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SuppliersDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SuppliersService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
