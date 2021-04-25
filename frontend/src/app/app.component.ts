import { Component } from '@angular/core';
import { ApplicationService, Application } from '../../src-gen';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  apps: Array<Application>;

  constructor(
    private serviceClient: ApplicationService,
    private logger: NGXLogger
  ) {}
  printApplications(): void {
    this.serviceClient.getApplications('test').subscribe(apps => {
      this.apps = apps;
      apps.forEach(app => this.logger.debug('Application:', app.name));
    });
  }
}
