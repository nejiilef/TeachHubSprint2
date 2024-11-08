import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/auth/service/auth.service';
import { IDevoir } from '../model/idevoir';
import { map, Observable } from 'rxjs';
import { IDevoirDTO } from '../model/idevoir-dto';
const BASE_URL = ["http://localhost:9099/"];
@Injectable({
  providedIn: 'root'
})
export class DevoirService {

  devoirs!:IDevoir[];
  constructor(private http:HttpClient,private service:AuthService) { }
  
  headers= this.service.createAuthorizationHeader()
  getAllDevoirs(idCours:number): Observable<IDevoir[]> {
    return this.http.get<IDevoir[]>(BASE_URL + "Devoirs/"+idCours, { headers: this.headers! });
  }
  addDevoir(formData: FormData, id: number): Observable<any> {
    return this.http.post<any>(BASE_URL + 'addDevoir/' + id, formData, { headers: this.headers! });
  }
  downloadDevoirPDF(devoirId: number): Observable<Blob> {
    return this.http.get(BASE_URL + `devoir/download/${devoirId}`, { responseType: 'blob' });
  }
  updateDevoir(devoir:IDevoirDTO , id : number):Observable<IDevoir>{
    return this.http.put<IDevoir>(BASE_URL+"updateDevoir/"+id,devoir,{headers:this.headers!})
  }
  deleteDevoir(id : number):Observable<string>{
    return this.http.delete(BASE_URL+"deleteDevoir/"+id,{headers:this.headers!, responseType: 'text'})
  }
  getDevoirById(id: number): Observable<IDevoir | null> {
    return this.getAllDevoirs(+localStorage.getItem("idCours")!).pipe(
      map(devoir => {
        this.devoirs=devoir;
        return this.devoirs.find( d=> d.idDevoir === id)||null;
  
       
      })
    );
  }

  getDevoirsByEtudiantId(email:string,idCours:number){
    return this.http.get<IDevoir[]>(BASE_URL + "devoir/etudiant/"+email+"/"+idCours, { headers: this.headers! });

  }

}
