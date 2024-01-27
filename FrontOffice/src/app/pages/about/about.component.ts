import { Component, Input } from '@angular/core';
import {TeamMemberComponent} from "../../components/team-member/team-member.component";

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css'],
})
export class AboutComponent {
  teamMember: any = [
    {
      imgPath: '../../../assets/images/team-1.jpg',
      name: 'Abc',
      designation: 'Advisor',
    },
    {
      imgPath: '../../../assets/images/team-2.jpg',
      name: 'XYZ',
      designation: 'Sale',
    },
    {
      imgPath: '../../../assets/images/team-3.jpg',
      name: 'MPM',
      designation: 'Manager',
    },
  ];
}
