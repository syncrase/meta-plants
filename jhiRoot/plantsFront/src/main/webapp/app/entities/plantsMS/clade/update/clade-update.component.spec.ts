jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CladeService } from '../service/clade.service';
import { IClade, Clade } from '../clade.model';

import { CladeUpdateComponent } from './clade-update.component';

describe('Clade Management Update Component', () => {
  let comp: CladeUpdateComponent;
  let fixture: ComponentFixture<CladeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cladeService: CladeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CladeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CladeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CladeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cladeService = TestBed.inject(CladeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const clade: IClade = { id: 456 };

      activatedRoute.data = of({ clade });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(clade));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Clade>>();
      const clade = { id: 123 };
      jest.spyOn(cladeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clade }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cladeService.update).toHaveBeenCalledWith(clade);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Clade>>();
      const clade = new Clade();
      jest.spyOn(cladeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clade }));
      saveSubject.complete();

      // THEN
      expect(cladeService.create).toHaveBeenCalledWith(clade);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Clade>>();
      const clade = { id: 123 };
      jest.spyOn(cladeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cladeService.update).toHaveBeenCalledWith(clade);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
