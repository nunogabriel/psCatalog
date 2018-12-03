/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PsCatalogTestModule } from '../../../test.module';
import { AddressesDeleteDialogComponent } from 'app/entities/addresses/addresses-delete-dialog.component';
import { AddressesService } from 'app/entities/addresses/addresses.service';

describe('Component Tests', () => {
    describe('Addresses Management Delete Component', () => {
        let comp: AddressesDeleteDialogComponent;
        let fixture: ComponentFixture<AddressesDeleteDialogComponent>;
        let service: AddressesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PsCatalogTestModule],
                declarations: [AddressesDeleteDialogComponent]
            })
                .overrideTemplate(AddressesDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AddressesDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AddressesService);
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
