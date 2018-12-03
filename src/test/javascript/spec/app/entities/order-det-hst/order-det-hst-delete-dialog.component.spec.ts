/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsCatalogTestModule } from '../../../test.module';
import { OrderDetHstDeleteDialogComponent } from 'app/entities/order-det-hst/order-det-hst-delete-dialog.component';
import { OrderDetHstService } from 'app/entities/order-det-hst/order-det-hst.service';

describe('Component Tests', () => {
    describe('OrderDetHst Management Delete Component', () => {
        let comp: OrderDetHstDeleteDialogComponent;
        let fixture: ComponentFixture<OrderDetHstDeleteDialogComponent>;
        let service: OrderDetHstService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrderDetHstDeleteDialogComponent]
            })
                .overrideTemplate(OrderDetHstDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrderDetHstDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrderDetHstService);
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
