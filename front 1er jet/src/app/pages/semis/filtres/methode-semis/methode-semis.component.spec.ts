import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MethodeSemisComponent } from './methode-semis.component';

describe('MethodeSemisComponent', () => {
  let component: MethodeSemisComponent;
  let fixture: ComponentFixture<MethodeSemisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MethodeSemisComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MethodeSemisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
