import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BurgertuesdayTestModule } from '../../../../test.module';
import { RestaurantDetailComponent } from 'app/admin/entities/restaurant/restaurant-detail.component';
import { Restaurant } from 'app/shared/model/restaurant.model';

describe('Component Tests', () => {
  describe('Restaurant Management Detail Component', () => {
    let comp: RestaurantDetailComponent;
    let fixture: ComponentFixture<RestaurantDetailComponent>;
    const route = ({ data: of({ restaurant: new Restaurant(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BurgertuesdayTestModule],
        declarations: [RestaurantDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RestaurantDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RestaurantDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.restaurant).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
