import { Component, Input } from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../../services/authservices';
import {ToastrService} from "ngx-toastr";
import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationModalComponent } from '../confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-change-password-modal-component',
  templateUrl: './change-password-modal-component.component.html',
  styleUrls: ['./change-password-modal-component.component.css']
})
export class ChangePasswordModalComponentComponent {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;

  constructor(public activeModal: NgbActiveModal, private authService: AuthService ,   private toastr: ToastrService ,   private modalService: NgbModal,
  ) {}
  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }
  openConfirmationModal() {
    const modalRef = this.modalService.open(ConfirmationModalComponent, {
      size: 'sm',
      centered: true // This will ensure the modal is vertically centered
    });
    modalRef.componentInstance.confirmEvent.subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.changePasswordConfirmed();
      }
    });
  }

  changePasswordConfirmed() {
    if (this.newPassword === this.confirmPassword && this.getUserId()) {
      const passwordData = {
        oldPassword: this.oldPassword,
        newPassword: this.newPassword
      };

      this.authService.changePassword(this.getUserId(), passwordData.oldPassword, passwordData.newPassword)
        .subscribe(
          result => {
            console.log(result);
            this.toastr.info(result, '', {
              positionClass: 'toast-top-right'
            });
            this.activeModal.close();
          },
          error => {
            console.error(error);
            this.toastr.error(error.error, 'Error', {
              positionClass: 'toast-top-right'
            });
          }
        );
    } else {
      // Consider adding logic to display a message if passwords don't match or userId is null
      this.toastr.error('The passwords do not match or user ID is missing.', 'Error', {
        positionClass: 'toast-top-right'
      });
    }
  }
}
