
import { Component, OnInit } from '@angular/core';
import { MariemService } from "../../services/mariem.service";
import {OrdreAchatLimiteDTO} from "../../entities/OrdreAchatLimiteDTO";
import {OrdreAchatAuMarcheDTO} from "../../entities/OrdreAchatAuMarcheDTO";
import {OrdreVenteLimiteDTO} from "../../entities/OrdreVenteLimiteDTO";
import {OrdreVenteAuMarcheDTO} from "../../entities/OrdreVenteAuMarcheDTO";
import {Observable} from "rxjs";


@Component({
  selector: 'app-ordrevente',
  templateUrl: './ordrevente.component.html',
  styleUrls: ['./ordrevente.component.css']
})
export class OrdreventeComponent implements OnInit{


  ordresAchatLimite: OrdreVenteLimiteDTO[] = [];
  ordresAchatAuMarche: OrdreVenteAuMarcheDTO[] = [];
  selectedType = 'limite';

  constructor(private mariemService: MariemService) {}

  ngOnInit() {
    this.mariemService.getOrdresVenteLimiteByClientId(this.getUserId()).subscribe((data: OrdreVenteLimiteDTO[]) => {
      this.ordresAchatLimite = data;
    });

    this.mariemService.getOrdresVenteAuMarcheByClientId(this.getUserId()).subscribe((data: OrdreVenteAuMarcheDTO[]) => {
      this.ordresAchatAuMarche = data;
    });
    this.getSelectedTypeData().subscribe((data) => {
      this.updateOrdreDataBasedOnType(data);
    });
  }
  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }

  annulerOrdreVente(idOrdre: number) {
    this.mariemService.annulerOrdreV(idOrdre).subscribe(
      response => {
        // Handle the response, e.g., refresh the list or show a message
      },
      error => {
        // Handle errors here
      }
    );
  }

  getSelectedTypeData(): Observable<any> {
    const id = this.getUserId();
    return this.selectedType === 'limite' ?
      this.mariemService.getOrdresVenteLimiteByClientId(id) :
      this.mariemService.getOrdresVenteAuMarcheByClientId(id);
  }

  // Call this method when the selectedType changes
  onTypeChange() {
    this.getSelectedTypeData().subscribe((data) => {
      this.updateOrdreDataBasedOnType(data);
    });
  }

  private updateOrdreDataBasedOnType(data: any) {
    if (this.selectedType === 'limite') {
      this.ordresAchatLimite = data;
    } else {
      this.ordresAchatAuMarche = data;
    }
  }


}

