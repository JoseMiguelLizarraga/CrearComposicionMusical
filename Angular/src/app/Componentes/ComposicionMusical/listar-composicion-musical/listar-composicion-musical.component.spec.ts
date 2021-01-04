import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarComposicionMusicalComponent } from './listar-composicion-musical.component';

describe('ListarComposicionMusicalComponent', () => {
  let component: ListarComposicionMusicalComponent;
  let fixture: ComponentFixture<ListarComposicionMusicalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListarComposicionMusicalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListarComposicionMusicalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
