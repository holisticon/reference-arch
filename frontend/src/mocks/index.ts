import { HttpClientTestingModule } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { ApplicationService } from '../../src-gen';
import { ApplicationServiceMock } from './application.service.mock';

export const MODULE_MOCKS = [
  HttpClientTestingModule,
  LoggerModule.forRoot({ level: NgxLoggerLevel.DEBUG, serverLogLevel: NgxLoggerLevel.OFF }),
];

export const SERVICE_MOCKS = [
  { provide: ApplicationService, useClass: ApplicationServiceMock },
];
