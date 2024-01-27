import { Component, OnInit } from '@angular/core';
import { Support } from 'app/Models/Support';
import { SupportService } from 'app/services/support.service';
import { Chart } from 'chart.js/auto';

@Component({
  selector: 'app-support',
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.scss']
})
export class SupportComponent implements OnInit {

//-------------------------------------------------------------------------------------
Support:Support;
listSupport:any;
Supportupdate:any;
SupportDetails:any;
list:any;
Event:Support; 
url: string | ArrayBuffer;
ttype:any; 
chart:any;
article: any;
video:any; 
livre:any;
constructor(private SupportService:SupportService) { 
  this.getAllSupport();
}

  ngOnInit():void {
  //  this.getAllSupport()
  this.getData();

    this.Support = {
      id: null,
      nom: null,
      description: null,
      date_publication:null,
      type:null,
      medialink:null
    
     }
     this.Supportupdate = {
      id: null,
      nom: null,
      description: null,
      date_publication:null,
      type:null
     }
  }
  
  getAllSupport() {
    this.SupportService.getAllSupport().subscribe(res => {
      this.listSupport = res;
      
    });
  }
  
  addSupport(){
    this.SupportService.addSupport(this.Support).subscribe(()=> this.getAllSupport());

  }

  deleteSupport(idSupport:number){
    this.SupportService.deleteSupport(idSupport).subscribe(()=>this.getAllSupport() ,res=>{this.listSupport=res});
     }

     edit(Event: any){
      this.Supportupdate = Event;
    }
    getActivitydetails(){
      this.SupportService.getSupport(this.Supportupdate.id).subscribe(res=>{this.SupportDetails=res});
    }
     updateSupport(){
      this.SupportService.updateSupport(this.Supportupdate).subscribe(
        (resp) => {
          console.log(resp);
        },
        (err) => {
          console.log(err);
        }
      );}
      readUrl(event:any) {

        if (event.target.files && event.target.files[0]) {
          var reader = new FileReader();
      
          reader.onload = (event: ProgressEvent) => {
            this.url = (<FileReader>event.target).result;
          }

          reader.readAsDataURL(event.target.files[0]);
        }
      }
      isImage(link: string): boolean {
        return link.endsWith('.jpg') || link.endsWith('.jpeg') || link.endsWith('.png') || link.endsWith('.webp');
    }
    getSupportBytype(){
      this.SupportService.getSupportBytype(this.ttype).subscribe(res=>{this.listSupport=res});
    }

    getData() {
      let ctx: any = document.getElementById("lineChart1") as HTMLElement;
         
      var data = {
        labels: ["Types des supports pédagogique"],
        datasets: [
          {
            label: "Type article",
            data: [],
            backgroundColor: "#b91d47",
            borderColor: "lightblue",
            fill: false,
            lineTension: 0,
            radius: 5
          },
          {
            label: "Type livre",
            data: [],
            backgroundColor: "#00aba9",
            borderColor: "lightgreen",
            fill: false,
            lineTension: 0,
            radius: 5
          },
          {
            label: "Type video",
            data: [],
            backgroundColor:   "#2b5797",
            borderColor: "lightyellow",
            fill: false,
            lineTension: 0,
            radius: 5
          }
        ]
    
      };
    
   //options
   var options = {
    responsive: true,
    title: {
      display: true,
      position: "top",
      text: "Line Graph",
      fontSize: 18,
      fontColor: "#111"
    },
    legend: {
      display: true,
      position: "bottom",
      labels: {
        fontColor: "#333",
        fontSize: 16
      }
    }
  };

  //create Chart class object
  this.chart = new Chart(ctx, {
    type: "bar",
    data: data,
    options: options
  });
  this.SupportService.getSupportBytypearticle().subscribe(res => {
  this.article = res;
  this.SupportService.getSupportBytypelivre().subscribe(res => {
  this.livre = res;
  this.SupportService.getSupportBytypevideo().subscribe(res => {
  this.video = res;
      this.chart.data.datasets[0].data.push(this.article);
      this.chart.data.datasets[1].data.push(this.livre);
      this.chart.data.datasets[2].data.push(this.video);
    });
  });
});
this.chart.update();
}

}


   
