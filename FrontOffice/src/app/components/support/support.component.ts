import { Component } from '@angular/core';
import { SupportService } from 'src/app/services/supportservice';

@Component({
  selector: 'app-support',
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.css']
})
export class SupportComponent {
  listSupport:any;
  page=1; 
  constructor(private SupportService:SupportService) { 
    this.getAllSupport();
  }
  
    ngOnInit():void {
  
    }
    
  getAllSupport() {
    this.SupportService.getAllSupport().subscribe(res => {
      this.listSupport = res; 
    });
  }
}
