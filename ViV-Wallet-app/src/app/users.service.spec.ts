import { TestBed } from '@angular/core/testing';
import { User } from './types';
import { UsersService } from './users.service';
import { LoginService } from './login.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthenticationInterceptor } from './http-interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { environment } from './../environments/environment';

describe('UsersService', () => {
    let httpTestingController: HttpTestingController;
    let service: UsersService;
    const token = 'token';
    const loginSpy = jasmine.createSpyObj('LoginService', { 'getJwtToken': token });

    const usersEndpoint = `${environment.backendUrl}/api/v1/users`;
    const prototypeUser: User = {
        id: '123',
        login: 'mylogin',
        fullname: 'My LOGIN',
        email: 'my.login@invivoo.com'
    };

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [
                { provide: LoginService, useValue: loginSpy },
                { provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptor, multi: true },
            ]
        });

        httpTestingController = TestBed.get(HttpTestingController);
        service = TestBed.get(UsersService);
    });

    afterEach(() => {
        httpTestingController.verify();
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    it('should post user if there\'s no id', () => {
        service.saveUser({ id: undefined } as User).subscribe(u => {
            expect(u).toEqual(prototypeUser);
        });

        const testRequest = httpTestingController.expectOne(usersEndpoint);
        expect(testRequest.request.method).toBe('POST');
        expect(testRequest.request.headers.has('Authorization')).toBeTruthy();
        expect(testRequest.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

        testRequest.flush(prototypeUser);
    });

    it('should update user if there\'s an id', () => {
        service.saveUser({ id: '123' } as User).subscribe(u => {
            expect(u).toEqual(prototypeUser);
        });

        const testRequest = httpTestingController.expectOne(usersEndpoint + '/123');
        expect(testRequest.request.method).toBe('PUT');
        expect(testRequest.request.headers.has('Authorization')).toBeTruthy();
        expect(testRequest.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

        testRequest.flush(prototypeUser);
    });
});
