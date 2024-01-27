import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FormationService {
  private API_URL = "http://localhost:8083/pi_back/formation";
  constructor(private httpClient: HttpClient) { }


  getAllFormation(){
    return this.httpClient.get(`${this.API_URL}/all`)
  }
  addFormation(formation : any) {
    return this.httpClient.post(`${this.API_URL}/add`, formation)
  }
  updateFormation(formation : any){
    return this.httpClient.put(`${this.API_URL}/update`,formation)
  }
  deleteFormation(Id : number){
    return  this.httpClient.delete(`${this.API_URL}/delete/${Id}`)
  }
 getFormation(Id : any){
    return  this.httpClient.get(`${this.API_URL}/get/${Id}`)
  }
  getFormationselonperiode(date_debut : any,date_fin : any){
    return  this.httpClient.get(`${this.API_URL}/betweendate/${date_debut}/${date_fin}`)
  }
  getListedesparticipants(idFormation : any){
    return  this.httpClient.get(`http://localhost:8083/pi_back/client/clients-with-formations/${idFormation}`)
  }
}
