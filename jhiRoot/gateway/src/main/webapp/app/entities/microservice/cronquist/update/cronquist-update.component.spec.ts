jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CronquistService } from '../service/cronquist.service';
import { ICronquist, Cronquist } from '../cronquist.model';

import { CronquistUpdateComponent } from './cronquist-update.component';

describe('Cronquist Management Update Component', () => {
  let comp: CronquistUpdateComponent;
  let fixture: ComponentFixture<CronquistUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cronquistService: CronquistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CronquistUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CronquistUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CronquistUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cronquistService = TestBed.inject(CronquistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cronquist: ICronquist = { id: 456 };

      activatedRoute.data = of({ cronquist });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cronquist));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cronquist>>();
      const cronquist = { id: 123 };
      jest.spyOn(cronquistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cronquist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cronquist }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cronquistService.update).toHaveBeenCalledWith(cronquist);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cronquist>>();
      const cronquist = new Cronquist();
      jest.spyOn(cronquistService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cronquist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cronquist }));
      saveSubject.complete();

      // THEN
      expect(cronquistService.create).toHaveBeenCalledWith(cronquist);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cronquist>>();
      const cronquist = { id: 123 };
      jest.spyOn(cronquistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cronquist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cronquistService.update).toHaveBeenCalledWith(cronquist);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
