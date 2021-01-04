import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Importacion de componentes
import { AuthGuardService } from './Servicios/auth-guard.service';
import { ListarComposicionMusicalComponent } from './Componentes/ComposicionMusical/listar-composicion-musical/listar-composicion-musical.component';
import { CrearEditarComposicionMusicalComponent } from './Componentes/ComposicionMusical/crear-editar-composicion-musical/crear-editar-composicion-musical.component';
import { LoginComponent } from './Componentes/login/login.component';


const routes: Routes = [
  {path: 'login', component:LoginComponent},
  {path: 'composicionMusical/listar', component: ListarComposicionMusicalComponent},
  {path: 'composicionMusical/crearEditar', component: CrearEditarComposicionMusicalComponent, canActivate: [AuthGuardService]},

  {path: '**', redirectTo: '/', pathMatch: 'full'}   // Con cualquier ruta desconocida te redirige al inicio
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
