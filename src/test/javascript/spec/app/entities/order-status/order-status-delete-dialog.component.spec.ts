/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsCatalogTestModule } from '../../../test.module';
import { OrderStatusDeleteDialogComponent } from 'app/entities/order-status/order-status-delete-dialog.component';
import { OrderStatusService } from 'app/entities/order-status/order-status.service';

describe('Component Tests', () => {
    describe('OrderStatus Management Delete Component', () => {
        let comp: OrderStatusDeleteDialogComponent;
        let fixture: ComponentFixture<OrderStatusDeleteDialogComponent>;
        let service: OrderStatusService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrderStatusDeleteDialogComponent]
            })
                .overrideTemplate(OrderStatusDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrderStatusDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrderStatusService);
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
