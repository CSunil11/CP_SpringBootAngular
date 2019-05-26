import { Component } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import * as ROUTS from './constants/routs';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  
  ROUTS : any;

	constructor(public router: Router) {
		this.ROUTS = ROUTS;
	}
}
