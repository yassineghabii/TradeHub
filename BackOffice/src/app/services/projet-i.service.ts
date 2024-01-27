import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProjetIService {

  private API_URL = "http://localhost:8083/pi_back/ProjetInvestissement";

  constructor(private httpClient: HttpClient) { }
  
  getAllProjetI(){
    return this.httpClient.get(`${this.API_URL}/all`)
  }
  addProjetI(projet : any) {
    return this.httpClient.post(`${this.API_URL}/add`, projet)
  }
  updateProjet(projet : any){
    return this.httpClient.put(`${this.API_URL}/update`,projet)
  }
  deleteProjet(Id : number){
    return  this.httpClient.delete(`${this.API_URL}/delete/${Id}`)
  }
 getProjet(Id : any){
    return  this.httpClient.get(`${this.API_URL}/get/${Id}`)
  }
  getProjetByLoca(localisation : any){
    return  this.httpClient.get(`${this.API_URL}/getbyloca/${localisation}`)
  }
}
