import { Component, OnInit } from '@angular/core';
import { IDevoirRendu } from '../model/idevoir-rendu';
import { DevoirRenduService } from '../service/devoir-rendu.service';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-list-devoir-rendu',
  templateUrl: './list-devoir-rendu.component.html',
  styleUrls: ['./list-devoir-rendu.component.css']
})
export class ListDevoirRenduComponent implements OnInit{
  listDevoirRendu!:any[];
  constructor(private service:DevoirRenduService,private router:Router,private activatedRoute:ActivatedRoute){}
   
  ngOnInit(): void {
    this.activatedRoute.params.subscribe((parametres) => {
      const id = +parametres['id']; // Convert the parameter to a number
      this.service.getAllDevoirsRendu(id).subscribe((dev) => {
        this.listDevoirRendu = dev;
        console.log(dev);
      });
    });
  }
  
 
   downloadRenduPDF(email:string): void {
     this.service.downloadDevoirRenduPDF(+localStorage.getItem("idDevoir")!,email).subscribe((blob: Blob) => {
       const url = window.URL.createObjectURL(blob);
       const a = document.createElement('a');
       a.href = url;
       a.download = `rendu_${localStorage.getItem("idDevoir")}.pdf`;
       a.click();
     });
   }
 }