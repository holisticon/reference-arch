import { Component, OnInit } from "@angular/core";
import { KeycloakService } from "keycloak-angular";
import { KeycloakProfile } from "keycloak-js";
import { NGXLogger } from "ngx-logger";
import { Application, ApplicationService } from "../../src-gen";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})
export class AppComponent implements OnInit {
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;

  apps: Array<Application>;

  constructor(
    private readonly keycloak: KeycloakService,
    private serviceClient: ApplicationService,
    private logger: NGXLogger
  ) {}
  
  printApplications(): void {
    this.serviceClient.getApplications("test").subscribe((apps) => {
      this.apps = apps;
      apps.forEach((app) => this.logger.debug("Application:", app.name));
    });
  }

  public async ngOnInit() {
    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();
    }
  }

  public login() {
    this.keycloak.login();
  }

  public logout() {
    this.keycloak.logout();
  }
}
