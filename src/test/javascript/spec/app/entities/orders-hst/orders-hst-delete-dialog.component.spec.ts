/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsCatalogTestModule } from '../../../test.module';
import { OrdersHstDeleteDialogComponent } from 'app/entities/orders-hst/orders-hst-delete-dialog.component';
import { OrdersHstService } from 'app/entities/orders-hst/orders-hst.service';

describe('Component Tests', () => {
    describe('OrdersHst Management Delete Component', () => {
        let comp: OrdersHstDeleteDialogComponent;
        let fixture: ComponentFixture<OrdersHstDeleteDialogComponent>;
        let service: OrdersHstService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [OrdersHstDeleteDialogComponent]
            })
                .overrideTemplate(OrdersHstDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrdersHstDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrdersHstService);
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
