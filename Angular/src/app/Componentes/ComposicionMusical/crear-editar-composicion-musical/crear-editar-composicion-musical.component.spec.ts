import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearEditarComposicionMusicalComponent } from './crear-editar-composicion-musical.component';

describe('CrearEditarComposicionMusicalComponent', () => {
  let component: CrearEditarComposicionMusicalComponent;
  let fixture: ComponentFixture<CrearEditarComposicionMusicalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrearEditarComposicionMusicalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CrearEditarComposicionMusicalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
