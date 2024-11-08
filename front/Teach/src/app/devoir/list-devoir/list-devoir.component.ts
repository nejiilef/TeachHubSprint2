import { Component, DoCheck, OnInit } from '@angular/core';
import { IDevoir } from '../model/idevoir';
import { DevoirService } from '../service/devoir.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DevoirRenduService } from 'src/app/devoir-rendu/service/devoir-rendu.service';
import { CoursService } from 'src/app/cours/service/cours.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-list-devoir',
  templateUrl: './list-devoir.component.html',
  styleUrls: ['./list-devoir.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class ListDevoirComponent implements OnInit, DoCheck {
  devoirs: IDevoir[] = [];
  documentsList: any[] = [];
  courId!: number;
  courseCode: string = '';
  courseNom: string ='';
  role: string | null = '';
  isPopupVisible = false;
  rendu = false;
  test!: boolean;
  pdfFile?: File;
  subbmited = false;
  studentEmail: string = '';
  teacherEmail: string = '';
  // Variables pour gérer les messages de succès et d'erreur
invitationStudentSuccess: string = '';
invitationStudentError: string = '';
invitationTeacherSuccess: string = '';
invitationTeacherError: string = '';
invitationStudentMessageClass: string = ''; // Classe CSS pour style de message
invitationTeacherMessageClass: string = ''; // Classe CSS pour style de message


  constructor(
    private devoirService: DevoirService,
    private devoirRService: DevoirRenduService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private coursService: CoursService,
    
  ) {}

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
    this.activatedRoute.params.subscribe((params) => {
      this.courId = +params['id'];
      localStorage.setItem("idCours", params['id']);
      
      if (this.role === 'etudiant') {
        this.devoirService.getDevoirsByEtudiantId(localStorage.getItem("username")!, this.courId)
          .subscribe((d) => this.devoirs = d || []);
      } else {
        this.devoirService.getAllDevoirs(this.courId)
          .subscribe((d) => this.devoirs = d || []);
        this.getCourseCode();
      }
      
      this.getDocuments();
    });
  }

  ngDoCheck(): void {
    const currentRoute = this.router.url;
    const role = localStorage.getItem('role');
    this.test = role !== 'etudiant';
  }

  confirm() {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: 'Please confirm to proceed moving forward.',
      acceptIcon: 'pi pi-check mr-2',
      rejectIcon: 'pi pi-times mr-2',
      rejectButtonStyleClass: 'p-button-sm',
      acceptButtonStyleClass: 'p-button-outlined p-button-sm',
      accept: () => {
        this.messageService.add({ severity: 'info', summary: 'Confirmed', detail: 'You have accepted', life: 3000 });
      },
      reject: () => {
        this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected', life: 3000 });
      }
    });
  }

  getDocuments() {
    this.coursService.getDocumentsByCourId(this.courId).subscribe(
      (documents) => this.documentsList = documents,
      (error) => console.error('Erreur lors de la récupération des documents :', error)
    );
  }

  viewDocument(documentId: number) {
    this.coursService.downloadDocument(documentId).subscribe(
      (blob) => {
        const url = window.URL.createObjectURL(blob);
        const iframe = document.createElement('iframe');
        iframe.style.display = 'none';
        iframe.src = url;
        document.body.appendChild(iframe);
        iframe.onload = () => {
          if (iframe.contentWindow) iframe.contentWindow.print();
        };
      },
      (error) => console.error('Erreur lors de la consultation du document :', error)
    );
  }

  showPopup(id: number) {
    if (this.role === "enseignant") {
      this.router.navigate(['/DevoirsRenduEnseignant']);
    } else {
      localStorage.setItem("idDevoir", id.toString());
      this.devoirRService.checkDevoirRendu(id, localStorage.getItem("username")!)
        .subscribe((r) => this.rendu = true);
      this.isPopupVisible = true;
    }
  }

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length) {
      this.pdfFile = input.files[0];
    }
  }

  closePopup() {
    this.isPopupVisible = false;
    this.rendu = false;
  }

  downloadPDF(devoirId: number): void {
    this.devoirService.downloadDevoirPDF(devoirId).subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `devoir_${devoirId}.pdf`;
      a.click();
    });
  }

  downloadRenduPDF(): void {
    this.devoirRService.downloadDevoirRenduPDF(+localStorage.getItem("idDevoir")!, localStorage.getItem("username")!)
      .subscribe((blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `rendu_${localStorage.getItem("idDevoir")}.pdf`;
        a.click();
      });
  }

  onSubmit(f: NgForm) {
    this.subbmited = true;
    if (f.invalid) return;
    this.addDevoirRendu(f);
  }

  addDevoirRendu(f: NgForm) {
    const formData = new FormData();
    if (this.pdfFile) formData.append('pdf', this.pdfFile);
    this.devoirRService.addDevoirRendu(formData, +localStorage.getItem("idDevoir")!, localStorage.getItem("username")!)
      .subscribe(() => {
        this.router.navigate(['/devoirs/list/' + localStorage.getItem("idCours")]);
        this.closePopup();
      });
  }

  updateDevoirRendu(f: NgForm) {
    const formData = new FormData();
    if (this.pdfFile) formData.append('pdf', this.pdfFile);
    this.devoirRService.updateDevoirRendu(formData, +localStorage.getItem("idDevoir")!, localStorage.getItem("username")!)
      .subscribe(() => this.router.navigate(['/devoirs/list/' + localStorage.getItem("idCours")]));
    this.closePopup();
  }

  deleteDevoirRendu() {
    this.devoirRService.deleteDevoirRendu(+localStorage.getItem("idDevoir")!, localStorage.getItem("username")!)
      .subscribe(() => {
        this.router.navigate(['/devoirs/list/' + localStorage.getItem("idCours")]);
        this.closePopup();
        this.ngOnInit();
        this.rendu = false;
      });
  }

  getCourseCode() {
    this.coursService.getCoursById(this.courId).subscribe(
      (course) => {
        if (course) this.courseCode = course.code;
        this.courseNom=course.nom;
      },
      (error) => console.error('Erreur lors de la récupération du code du cours :', error)
    );
  }

  inviteStudent() {
    this.coursService.inviteStudentByEmail(this.courseCode, this.studentEmail).subscribe(
      () => {
        this.invitationStudentSuccess = "L'étudiant a été invité avec succès";
        this.invitationStudentMessageClass = 'success-message'; // Classe CSS pour succès
        this.studentEmail = '';
      },
      (error) => {
        this.invitationStudentError = "L'invitation a échoué";
        this.invitationStudentMessageClass = 'error-message'; // Classe CSS pour erreur
      }
    );
  }
  
  inviteTeacher() {
    this.coursService.inviteTeacherByEmail(this.courseCode, this.teacherEmail).subscribe(
      () => {
        this.invitationTeacherSuccess = "L'enseignant a été invité avec succès";
        this.invitationTeacherMessageClass = 'success-message'; // Classe CSS pour succès
        this.teacherEmail = '';
      },
      (error) => {
        this.invitationTeacherError = "L'invitation a échoué";
        this.invitationTeacherMessageClass = 'error-message'; // Classe CSS pour erreur
      }
    );
  }
  
}
