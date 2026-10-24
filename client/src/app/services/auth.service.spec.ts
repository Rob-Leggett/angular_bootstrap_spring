import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let http: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(AuthService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should only complete login once the current user has been loaded', () => {
    let completed = false;
    service.login('user@tester.com.au', 'password').subscribe(() => (completed = true));

    http.expectOne('/api/auth/login').flush('', { headers: { 'X-AUTH-TOKEN': 'token' } });
    expect(completed).toBe(false);
    expect(service.isAuthenticated()).toBe(false);

    http.expectOne('/api/user').flush({ email: 'user@tester.com.au' });
    expect(completed).toBe(true);
    expect(service.isAuthenticated()).toBe(true);
    expect(service.getToken()).toBe('token');
  });

  it('should report no session when there is no token', () => {
    let result: boolean | undefined;
    service.restoreSession().subscribe(value => (result = value));
    expect(result).toBe(false);
  });

  it('should restore the session from a stored token', () => {
    localStorage.setItem('X-AUTH-TOKEN', 'stored');
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    const restored = TestBed.inject(AuthService);
    const controller = TestBed.inject(HttpTestingController);

    let result: boolean | undefined;
    restored.restoreSession().subscribe(value => (result = value));
    controller.expectOne('/api/user').flush({ email: 'user@tester.com.au' });

    expect(result).toBe(true);
    expect(restored.isAuthenticated()).toBe(true);
    controller.verify();
  });
});
