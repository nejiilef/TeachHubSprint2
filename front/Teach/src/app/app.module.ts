import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import{HttpClientModule} from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ButtonModule } from 'primeng/button';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { MessageService } from 'primeng/api';

import { AddEtudiantSousGroupeComponent } from './sous-groupe/add-etudiant-sous-groupe/add-etudiant-sous-groupe.component';
@NgModule({
  declarations: [
    AppComponent,
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    ButtonModule,
     TieredMenuModule


  ],
  providers: [MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
