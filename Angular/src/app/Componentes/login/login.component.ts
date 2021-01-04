import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AppComponent } from 'src/app/app.component';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit 
{

  constructor(public componentePrincipal: AppComponent) { }

  ngOnInit() {
  }

  loguearse(evento, datosForm:NgForm) 
  {
    evento.preventDefault();
    let jsonDatosForm = datosForm.form.value;
    this.componentePrincipal.loguearse(jsonDatosForm);
  }

}
