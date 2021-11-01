import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { GatewayTestModule } from '../../../../test.module';
import { NomVernaculaireComponent } from 'app/entities/microservice/nom-vernaculaire/nom-vernaculaire.component';
import { NomVernaculaireService } from 'app/entities/microservice/nom-vernaculaire/nom-vernaculaire.service';
import { NomVernaculaire } from 'app/shared/model/microservice/nom-vernaculaire.model';

describe('Component Tests', () => {
  describe('NomVernaculaire Management Component', () => {
    let comp: NomVernaculaireComponent;
    let fixture: ComponentFixture<NomVernaculaireComponent>;
    let service: NomVernaculaireService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [NomVernaculaireComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(NomVernaculaireComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NomVernaculaireComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NomVernaculaireService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new NomVernaculaire(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.nomVernaculaires && comp.nomVernaculaires[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new NomVernaculaire(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.nomVernaculaires && comp.nomVernaculaires[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
