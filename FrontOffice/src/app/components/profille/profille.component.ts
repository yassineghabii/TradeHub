import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/authservices';
import {Client} from "../../entities/Client";
import {NgbActiveModal, NgbModalOptions} from "@ng-bootstrap/ng-bootstrap"; // Make sure this path is correct
import { ChangePasswordModalComponentComponent } from '../change-password-modal-component/change-password-modal-component.component'; // Make sure the path is correct
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-profille',
  templateUrl: './profille.component.html',
  styleUrls: ['./profille.component.css',]
})
export class ProfilleComponent implements OnInit {

  clientProfile: Client = new Client(); // Supposons que Client est une classe avec toutes les propriétés appropriées.

  constructor(private authService: AuthService, private modalService: NgbModal) {} // Added modalService here
  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }

  ngOnInit(): void {


    const userId = this.getUserId();

    if (userId !== null) {
      this.authService.getClientProfile(userId).subscribe(
        (profile) => {
          this.clientProfile = profile;
          console.log('Client Profile Data:', profile); // Logging the response
        },
        (error) => {
          // Handle the error here
          console.error('There was an error fetching the client profile!', error);
        }
      );
    } else {
      console.error('No user ID found in the session storage.');
    }
  }
  openChangePasswordModal() {
    const modalRef = this.modalService.open(ChangePasswordModalComponentComponent, {
      centered: true // this option should center the modal
    } as NgbModalOptions);
    modalRef.componentInstance.userId = this.getUserId();
    modalRef.result.then(
      (result) => {
        // Handle result if needed
      },
      (reason) => {
        // Handle dismissal if needed
      }
    );
  }

}
