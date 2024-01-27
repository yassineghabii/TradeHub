import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SupportService {
  private API_URL = "http://localhost:8083/pi_back/SupportP";

  constructor(private httpClient: HttpClient) { }
  
  getAllSupport(){
    return this.httpClient.get(`${this.API_URL}/all`)
  }
  addSupport(support : any) {
    return this.httpClient.post(`${this.API_URL}/add`, support)
  }
  updateSupport(support : any){
    return this.httpClient.put(`${this.API_URL}/update`,support)
  }
  deleteSupport(Id : number){
    return  this.httpClient.delete(`${this.API_URL}/delete/${Id}`)
  }
 getSupport(Id : any){
    return  this.httpClient.get(`${this.API_URL}/get/${Id}`)
  }
  getSupportBytype(type : any){
    return  this.httpClient.get(`${this.API_URL}/getpartype/${type}`)
  }
  getSupportBytypevideo(){
    return  this.httpClient.get(`${this.API_URL}/getTypeVideo`)
  }
  getSupportBytypelivre(){
    return  this.httpClient.get(`${this.API_URL}/getTypeLivre`)
  }
  getSupportBytypearticle(){
    return  this.httpClient.get(`${this.API_URL}/getTypeArticle`)
  }

}
