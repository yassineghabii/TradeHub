import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {ProfilleComponent} from "../components/profille/profille.component";

@Injectable({ providedIn: 'root' })
export class ProfileModalGuard implements CanActivate {

  constructor(private modalService: NgbModal, private router: Router) {}

  canActivate(): boolean {
    const modalRef = this.modalService.open(ProfilleComponent, { size: 'lg' }); // Open the modal
    modalRef.result.then(result => {
      this.router.navigate([{ outlets: { modal: null } }]); // Close the modal outlet when the modal is closed
    }, reason => {
      this.router.navigate([{ outlets: { modal: null } }]);
    });

    return false; // Prevent navigation to the actual route
  }
}
