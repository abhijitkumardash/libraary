jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, waitForAsync, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';

import { BookItemService } from '../service/book-item.service';

import { BookItemDeleteDialogComponent } from './book-item-delete-dialog.component';

describe('BookItem Management Delete Component', () => {
  let comp: BookItemDeleteDialogComponent;
  let fixture: ComponentFixture<BookItemDeleteDialogComponent>;
  let service: BookItemService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BookItemDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(BookItemDeleteDialogComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookItemDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BookItemService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of({}));

        // WHEN
        comp.confirmDelete('bookItem');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('bookItem');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));
  });
}

