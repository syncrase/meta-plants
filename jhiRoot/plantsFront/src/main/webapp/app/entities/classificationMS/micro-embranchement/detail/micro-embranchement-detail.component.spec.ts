import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MicroEmbranchementDetailComponent } from './micro-embranchement-detail.component';

describe('MicroEmbranchement Management Detail Component', () => {
  let comp: MicroEmbranchementDetailComponent;
  let fixture: ComponentFixture<MicroEmbranchementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MicroEmbranchementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ microEmbranchement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MicroEmbranchementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MicroEmbranchementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load microEmbranchement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.microEmbranchement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
