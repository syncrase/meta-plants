import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousTribuDetailComponent } from './sous-tribu-detail.component';

describe('SousTribu Management Detail Component', () => {
  let comp: SousTribuDetailComponent;
  let fixture: ComponentFixture<SousTribuDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousTribuDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousTribu: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousTribuDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousTribuDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousTribu on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousTribu).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
