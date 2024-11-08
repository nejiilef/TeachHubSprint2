import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddEtudiantSousGroupeComponent } from './add-etudiant-sous-groupe.component';
import { SousGroupeService } from '../service/sous-groupe.service';
import { CoursService } from 'src/app/cours/service/cours.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { NgForm } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ICours } from 'src/app/cours/model/icours';

describe('AddEtudiantSousGroupeComponent', () => {
  let component: AddEtudiantSousGroupeComponent;
  let fixture: ComponentFixture<AddEtudiantSousGroupeComponent>;
  let sousGroupeService: SousGroupeService;
  let coursService: CoursService;
  let activatedRoute: ActivatedRoute;
  let router: Router;

  beforeEach(() => {
    // Mocking the services and ActivatedRoute
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [AddEtudiantSousGroupeComponent],
      providers: [
        { provide: SousGroupeService, useValue: jasmine.createSpyObj('SousGroupeService', ['getSousgroupeById', 'addEtudiantSousGroupe']) },
        { provide: CoursService, useValue: jasmine.createSpyObj('CoursService', ['getCoursById']) },
        { provide: ActivatedRoute, useValue: { params: of({ id: 1 }) } } // Mocking the route params
      ]
    });

    fixture = TestBed.createComponent(AddEtudiantSousGroupeComponent);
    component = fixture.componentInstance;
    sousGroupeService = TestBed.inject(SousGroupeService);
    coursService = TestBed.inject(CoursService);
    activatedRoute = TestBed.inject(ActivatedRoute);
    router = TestBed.inject(Router);
    
    // Mocking data for testing
    const mockCours: ICours = {
      idCours: 101,
      nom: 'Mathematics',
      coefficient: 3,
      credits: 6,
      code:'123',

      methodeCalcul:"",
      enseignant:{ id: 0,
        nom: '',
        prenom: '',
        email: '',
        motDePasse: '',
        specialite: null} ,
      students: [
        { id: 1, nom: 'Student 1', email: 'student1@example.com',prenom:'std1',motDePasse:"1234" },
        { id: 2, nom: 'Student 2', email: 'student2@example.com',prenom:'std2',motDePasse:"12345" }
      ]
    };

    const mockSousGroupe = {
      idSousGroupe: 1,
      nom: 'Group A',
      etudiants: [
          'student1@example.com' 
      ]
    }

    spyOn(coursService, 'getCoursById').and.returnValue(of(mockCours));
    spyOn(sousGroupeService, 'getSousgroupeById').and.returnValue(of(mockSousGroupe));
    spyOn(sousGroupeService, 'addEtudiantSousGroupe').and.returnValue(of({}));
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch all students and filter those not in the sous-groupe', () => {
    component.ngOnInit();
    
    expect(coursService.getCoursById).toHaveBeenCalled();
    expect(sousGroupeService.getSousgroupeById).toHaveBeenCalledWith(1);
    
    expect(component.etudiants).toEqual([
      { id: 2, name: 'Student 2', email: 'student2@example.com' }
    ]);
  });

  it('should add student to sous-groupe', () => {
    component.selectedEmail = 'student2@example.com';
    const form: NgForm = {} as NgForm;  // Mock form object

    component.ajouterEtudiant(form);
    
    expect(sousGroupeService.addEtudiantSousGroupe).toHaveBeenCalledWith(1, 'student2@example.com');
  });

  it('should handle errors when adding a student to the sous-groupe', () => {
    const errorResponse = new Error('An error occurred');
    spyOn(sousGroupeService, 'addEtudiantSousGroupe').and.returnValue(of(errorResponse));

    component.selectedEmail = 'student2@example.com';
    const form: NgForm = {} as NgForm;  // Mock form object

    component.ajouterEtudiant(form);
    
    expect(sousGroupeService.addEtudiantSousGroupe).toHaveBeenCalledWith(1, 'student2@example.com');
    // You can also check if the error is logged, for example:
    // expect(console.error).toHaveBeenCalledWith('Erreur lors de l\'ajout de l\'étudiant', errorResponse);
  });
});
