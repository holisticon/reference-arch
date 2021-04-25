import { getTestBed, TestBed } from "@angular/core/testing";
import { MODULE_MOCKS, SERVICE_MOCKS } from "../../mocks";
import { ConfigurationService } from "./configuration.service";

describe("ConfigurationService", () => {
  let injector: TestBed;
  let service: ConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [...MODULE_MOCKS],
      providers: [
        ...SERVICE_MOCKS,
        // neede to resolve Can't resolve all parameters for ConfigurationService: (?)
        { provide: ConfigurationService, useValue: new ConfigurationService() }
      ]
    }).compileComponents();
    injector = getTestBed();
    service = injector.get(ConfigurationService);
  });

  it("should init", () => {
    expect(service).toBeDefined();
  });
});
