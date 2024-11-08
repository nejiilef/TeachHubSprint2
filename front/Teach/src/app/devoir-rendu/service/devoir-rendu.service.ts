import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/auth/service/auth.service';
import { IDevoirRendu } from '../model/idevoir-rendu';
import { map, Observable } from 'rxjs';

const BASE_URL = ["http://localhost:9099/"];
@Injectable({
  providedIn: 'root'
})
export class DevoirRenduService {

  devoirs!:IDevoirRendu[];
  constructor(private http:HttpClient,private service:AuthService) { }
  headers= this.service.createAuthorizationHeader()
  getAllDevoirsRendu(idDevoirs:number): Observable<IDevoirRendu[]> {
    return this.http.get<IDevoirRendu[]>(BASE_URL + "DevoirRendu/"+idDevoirs, { headers: this.headers! });
  }
  addDevoirRendu(formData: FormData, id: number,email:string): Observable<any> {
    return this.http.post<any>(BASE_URL + 'addDevoirRendu/' + id+'/'+email, formData, { headers: this.headers! });
  }
  downloadDevoirRenduPDF(devoirId: number,email:string): Observable<Blob> {
    return this.http.get(BASE_URL + `devoirRendu/download/${devoirId}/${email}`, { responseType: 'blob' });
  }
  updateDevoirRendu(formData: FormData , id : number,email:string):Observable<IDevoirRendu>{
    return this.http.put<IDevoirRendu>(BASE_URL+"updateDevoirRendu/"+id+"/"+email,formData,{headers:this.headers!})
  }
  deleteDevoirRendu(id : number,email:string):Observable<string>{
    return this.http.delete(BASE_URL+"deleteDevoirRendu/"+id+"/"+email,{headers:this.headers!, responseType: 'text'})
  }
  getDevoirRenduById(id: number): Observable<IDevoirRendu | null> {
    return this.getAllDevoirsRendu(+localStorage.getItem("idDevoir")!).pipe(
      map(devoir => {
        this.devoirs=devoir;
        return this.devoirs.find( d=> d.idDevoirRendu === id)||null;
       
      })
    );
  }
 
  checkDevoirRendu(id: number,email:string){
    return this.http.get(BASE_URL + `CheckDevoirRendu/${id}/${email}`,{headers:this.headers!, responseType: 'text'});
  }
}