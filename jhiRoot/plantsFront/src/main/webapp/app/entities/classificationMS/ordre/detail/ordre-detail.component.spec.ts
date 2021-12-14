import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrdreDetailComponent } from './ordre-detail.component';

describe('Ordre Management Detail Component', () => {
  let comp: OrdreDetailComponent;
  let fixture: ComponentFixture<OrdreDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrdreDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ordre: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrdreDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrdreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ordre on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ordre).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
