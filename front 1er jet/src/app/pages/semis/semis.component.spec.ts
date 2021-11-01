import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SemisComponent } from './semis.component';

describe('SemisComponent', () => {
  let component: SemisComponent;
  let fixture: ComponentFixture<SemisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SemisComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SemisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
