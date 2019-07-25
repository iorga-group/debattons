import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';

import { DebattonsTestModule } from '../../../test.module';
import { DbtMetricsMonitoringComponent } from 'app/admin/metrics/metrics.component';
import { DbtMetricsService } from 'app/admin/metrics/metrics.service';

describe('Component Tests', () => {
  describe('DbtMetricsMonitoringComponent', () => {
    let comp: DbtMetricsMonitoringComponent;
    let fixture: ComponentFixture<DbtMetricsMonitoringComponent>;
    let service: DbtMetricsService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [DebattonsTestModule],
        declarations: [DbtMetricsMonitoringComponent]
      })
        .overrideTemplate(DbtMetricsMonitoringComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(DbtMetricsMonitoringComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DbtMetricsService);
    });

    describe('refresh', () => {
      it('should call refresh on init', () => {
        // GIVEN
        const response = {
          timers: {
            service: 'test',
            unrelatedKey: 'test'
          },
          gauges: {
            'jcache.statistics': {
              value: 2
            },
            unrelatedKey: 'test'
          }
        };
        spyOn(service, 'getMetrics').and.returnValue(of(response));

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.getMetrics).toHaveBeenCalled();
      });
    });
  });
});
