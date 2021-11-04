import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RaunkierDetailComponent } from './raunkier-detail.component';

describe('Raunkier Management Detail Component', () => {
  let comp: RaunkierDetailComponent;
  let fixture: ComponentFixture<RaunkierDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RaunkierDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ raunkier: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RaunkierDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RaunkierDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load raunkier on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.raunkier).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
