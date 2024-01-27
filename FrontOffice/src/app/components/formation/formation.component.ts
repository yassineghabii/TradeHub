import { Component } from '@angular/core';
import { Formation } from 'src/app/entities/Formation';
import { AuthService } from 'src/app/services/authservices';
import { FormationService } from 'src/app/services/formationservice';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-formation',
  templateUrl: './formation.component.html',
  styleUrls: ['./formation.component.css']
})
export class FormationComponent {
  Formation:Formation;
  listFormation:any;
  page = 1;
  idClient:any;
  participated = false;
  part:any;
  constructor(private FormationService:FormationService) { 
    this.getAllFormation();
  }
  ngOnInit():void {
      this.Formation = {
        id: null,
        nom: null,
        description: null,
        thematique:null, 
        capacite:null, 
        date_debut:null,
        date_fin:null, 
        organisateur:null
       }

      }

      getUserId(): number | null {
        const userIdStr = sessionStorage.getItem('id');
        return userIdStr ? Number(userIdStr) : null;
      }

      

  getAllFormation() {
    this.FormationService.getAllFormation().subscribe(res => {
      this.listFormation = res;      
    });
  }
  participer(id:any){
    this.participated = true;
    const userId = this.getUserId();
    this.FormationService.participer(userId,id).subscribe(  (res) => {
      // Si la réponse renvoie un Account, l'opération a réussi
      Swal.fire({
        position: 'top-end',
        icon: 'success',
        title: 'L\'opération a été effectuée avec succès!',
        showConfirmButton: true,
        timer: 1500
      });
    },
    (err) => {
      // Si la réponse renvoie une erreur, afficher un message d'erreur correspondant
      if (err.status === 400 && err.error.message === 'Account already assigned to InternalService') {
        Swal.fire({
          position: 'top-end',
          icon: 'warning',
          title: 'Une erreur est produite ! ',
          showConfirmButton: false,
          timer: 1500
        });
      } else {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Vous participez déja à cette formation !',
          showConfirmButton: false,
          timer: 1500
        });
      }
    });

  }
  annulerparticiper(id:any){
    this.participated = false;
    const userId = this.getUserId();
    this.FormationService.annulerparticiper(userId,id).subscribe(  (res) => {
      // Si la réponse renvoie un Account, l'opération a réussi
      Swal.fire({
        position: 'top-end',
        icon: 'success',
        title: 'Votre participation est annulée avec succés!',
        showConfirmButton: true,
        timer: 1500
      });
    },
    (err) => {
      // Si la réponse renvoie une erreur, afficher un message d'erreur correspondant
      if (err.status === 400 && err.error.message === 'Account not assigned to InternalService') {
        Swal.fire({
          position: 'top-end',
          icon: 'warning',
          title: 'Une erreur est produite ! ',
          showConfirmButton: false,
          timer: 1500
        });
      } else {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Vous ne participez pas à cette formation !',
          showConfirmButton: false,
          timer: 1500
        });
      }
    });

  }

  votelike(id: any) {
    const userId = this.getUserId();
    this.FormationService.voteLike(id, userId).subscribe((res) => {
      this.part = res;
      if (this.part === null) {
        // Si la réponse est nulle, afficher un message d'erreur
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Vous avez déjà voté pour cet événement!'
        });
      } else {
        // Si la réponse n'est pas nulle et renvoie un code HTTP 200, afficher un message de succès
        Swal.fire({
          icon: 'success',
          title: 'Succès!',
          text: 'Votre vote a été enregistré avec succès!'
        });
      }
    });
  }
  votedislike(id: any) {
    const userId = this.getUserId();
    this.FormationService.voteDislike(id, userId).subscribe((res) => {
      this.part = res;
      if (this.part === null) {
        // Si la réponse est nulle, afficher un message d'erreur
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Vous avez déjà voté pour cet événement!'
        });
      } else {
        // Si la réponse n'est pas nulle et renvoie un code HTTP 200, afficher un message de succès
        Swal.fire({
          icon: 'success',
          title: 'Succès!',
          text: 'Votre vote a été enregistré avec succès!'
        });
      }
    });
  }

}
