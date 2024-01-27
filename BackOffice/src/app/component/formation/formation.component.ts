import { Component } from '@angular/core';
import { Formation } from 'app/Models/Formation';
import { FormationService } from 'app/services/formation.service';
import { CalendarOptions, DateSelectArg, EventClickArg, EventApi, CalendarApi,EventInput } from '@fullcalendar/core';
import { Chart } from 'chart.js/auto';

@Component({
  selector: 'app-formation',
  templateUrl: './formation.component.html',
  styleUrls: ['./formation.component.scss']
})
export class FormationComponent {
  [x: string]: any;
  calendarVisible = true;
  
  currentEvents: EventApi[] = [];
  event: any;
 
  handleCalendarToggle() {
    this.calendarVisible = !this.calendarVisible;
  }

  handleWeekendsToggle() {
    const { calendarOptions } = this;
    calendarOptions.weekends = !calendarOptions.weekends;
  }
  handleDateSelect(selectInfo: DateSelectArg) {
    const nom = prompt('Please enter the name of the formation');
    if (nom === null) {
      return; // L'utilisateur a annulé la saisie
    }
  
    const thematique = prompt('Please enter the thematique of the formation');
    if (thematique === null) {
      return;
    }
  
    const description = prompt('Please enter the description of the formation');
    if (description === null) {
      return;
    }
  
    const date_debut = selectInfo.startStr;
    const date_fin = prompt('Please enter the end date of the formation');
    if (date_fin === null) {
      return;
    }
  
    const capaciteStr = prompt('Please enter the maximum capacity of the formation');
    if (capaciteStr === null) {
      return;
    }
    const capacite = parseInt(capaciteStr);
  
    const organisateur = prompt('Please enter the organizer of the formation');
    if (organisateur === null) {
      return;
    }
  
    const newFormation: Formation = {
      id:40, // L'ID peut être généré automatiquement
      nom,
      thematique,
      description,
      date_debut,
      date_fin,
      capacite,
      organisateur
    };
  
    // Ensuite, vous pouvez ajouter la nouvelle formation et effectuer d'autres opérations nécessaires
    this.FormationService.addFormation(newFormation).subscribe(() => this.getAllFormation());
  
    // Vous pouvez également ajouter la nouvelle formation à votre calendrier
    const calendarApi = selectInfo.view.calendar;
    calendarApi.unselect(); // Clear date selection
    calendarApi.addEvent({
      title: nom,
      start: date_debut,
      //end: date_fin,
     // allDay: selectInfo.allDay
    });
  }
 

  handleEventClick(clickInfo: EventClickArg) {
    if (confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {
      clickInfo.event.remove();

    }
  }

  handleEvents(events: EventApi[]) {
    this.currentEvents = events;
  } 

//-------------------------------------------------------------------------------------
  Formation:Formation;
  listFormation:any;
  Formationupdate:any;
  FormationDetails:any;
  list:any;
  Event:Formation; 
  calendarEvents: EventInput[] = [];
  date_Debut:any; 
  date_Fin:any; 
  chart: any;
  formations: Formation[] = [];
  ListParticipant:any; 

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
       this.Formationupdate = {
        id: null,
        nom: null,
        description: null,
        thematique:null, 
        capacite:null, 
        date_debut:null,
        date_fin:null, 
        organisateur:null
       }
       this.createChart();
       this.getData();
    }
    calendarOptions: CalendarOptions= {
      headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
      },
      initialView: 'dayGridMonth',
      eventBackgroundColor: 'blue', // Couleur de fond des événements
    eventBorderColor: 'black',    // Couleur de la bordure des événements
      // alternatively, use the `events` setting to fetch from a feed
      weekends: true,
      editable: true,
      selectable: true,
      selectMirror: true,
      dayMaxEvents: true,
      select: this.handleDateSelect.bind(this),
      eventClick: this.handleEventClick.bind(this),
      eventsSet: this.handleEvents.bind(this)     };

      createEvent(event:Formation) {
        let newEvent = {
          title: event.nom,
          date: event.date_debut
        };
        this.calendarOptions.events = [newEvent];
      }  
      createEvent1(event:Formation[]) {
        this.list = [];
        for(let i = 0; i < event.length; i++){
          let newEvent = {
            title: event[i].nom,
            date: event[i].date_debut
          };
          this.list.push(newEvent);
        }
        this.calendarOptions.events = this.list;
      }
  getAllFormation() {
    this.FormationService.getAllFormation().subscribe(res => {
      this.listFormation = res;
      this.createEvent1(this.listFormation);
      
    });
  }
  
  getParticipant(id:any) {
    this.FormationService.getListedesparticipants(id).subscribe(res => {
      this.ListParticipant = res;     
    });
  }

  addFormation(){
    this.FormationService.addFormation(this.Formation).subscribe(()=> this.getAllFormation());
    this.createEvent(this.Event);
  }

  deleteFormation(idFormation:number){
    this.FormationService.deleteFormation(idFormation).subscribe(()=>this.getAllFormation() ,res=>{this.listFormation=res});
     }

     edit(Event: any){
      this.Formationupdate = Event;
    }
    getActivitydetails(){
      this.FormationService.getFormation(this.Formationupdate.id).subscribe(res=>{this.FormationDetails=res});
    }
     updateFormation(){
      this.FormationService.updateFormation(this.Formationupdate).subscribe(
        (resp) => {
          console.log(resp);
        },
        (err) => {
          console.log(err);
        }
      );}
  
      getFormationPeriode(date_Debut:any,date_Fin:any){
        this.FormationService.getFormationselonperiode(date_Debut,date_Fin)
        .subscribe((res) => {
          this.listFormation = res;
        });     }

        
  createChart() {
    let ctx: any = document.getElementById("formationChart") as HTMLCanvasElement;

    // Obtenez vos données de formation et formatez-les pour le graphique
    const data = this.formatFormationData(this.formations);

    const chartData = {
      labels: data.labels,
      datasets: [
        {
          label: "Nombre de Formations",
          data: data.data,
          backgroundColor: "#b91d47",
          borderColor: "lightblue",
          fill: false,
          lineTension: 0,
          radius: 5
        }
      ]
    };

    const options = {
      responsive: true,
      title: {
        display: true,
        position: "top",
        text: "Formations par date",
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
      },   
      scales: {
        y: {
          beginAtZero: true, 
          stepSize: 1, 
          callback: function (value) {
            return Number.isInteger(value) ? value : ''; 
          }
        }
      }
    };

    this.chart = new Chart(ctx, {
      type: "line",
      data: chartData,
      options: options
    });
  }

  getData() {
    this.FormationService.getAllFormation().subscribe((formations: Formation[]) => {
      this.formations = formations;
      this.chart.data.datasets[0].data = this.formatFormationData(formations).data;
      this.chart.data.labels = this.formatFormationData(formations).labels;
      this.chart.update();
    });
  }

  formatFormationData(formations: Formation[]): { labels: string[], data: number[] } {
    const labelCounts: { [date: string]: number } = {};
  
    formations.forEach((formation: Formation) => {
      const date = formation.date_debut;
  
      if (labelCounts[date]) {
        labelCounts[date]++;
      } else {
        labelCounts[date] = 1;
      }
    });
  
    const labels = Object.keys(labelCounts);
    const data = Object.values(labelCounts);
  
    return { labels, data };
  }
  

    
}
