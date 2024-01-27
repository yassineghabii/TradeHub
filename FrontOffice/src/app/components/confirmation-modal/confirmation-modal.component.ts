import { Component, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html'
  // styleUrls if needed
})
export class ConfirmationModalComponent {
  @Output() confirmEvent = new EventEmitter<boolean>();

  constructor(public activeModal: NgbActiveModal) {}

  confirm() {
    this.confirmEvent.emit(true);
    this.activeModal.close();
  }

  decline() {
    this.confirmEvent.emit(false);
    this.activeModal.close();
  }
}
