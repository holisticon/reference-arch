import { HttpClientModule } from '@angular/common/http';
import { APP_INITIALIZER, NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { KeycloakAngularModule, KeycloakService } from "keycloak-angular";
// Logger
import { LoggerModule, NgxLoggerLevel } from "ngx-logger";
// PrimeFaces Modules
import { ButtonModule } from "primeng/button";
import { ApiModule } from "../../src-gen";
import { environment } from "../environments/environment";
import { BASE_PATH as backendUrl } from "./../../src-gen/variables";
// Our Modules
import { AppComponent } from "./app.component";

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: environment.authentication.keycloak.config,
    });
}
@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    KeycloakAngularModule,
    ButtonModule,
    LoggerModule.forRoot({
      level: NgxLoggerLevel.DEBUG,
      serverLogLevel: NgxLoggerLevel.OFF,
    }),
    ApiModule,
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService],
    },
    { provide: backendUrl, useValue: "http://localhost:8080" },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
