import { Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, tap, catchError, of, map } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = '/api';
  private readonly TOKEN_KEY = 'X-AUTH-TOKEN';
  
  private currentUserSignal = signal<User | null>(null);
  private tokenSignal = signal<string | null>(null);
  
  readonly currentUser = this.currentUserSignal.asReadonly();
  readonly isAuthenticated = computed(() => this.currentUserSignal() !== null);

  constructor(private http: HttpClient) {
    this.loadStoredAuth();
  }

  private loadStoredAuth(): void {
    const token = localStorage.getItem(this.TOKEN_KEY);
    if (token) {
      this.tokenSignal.set(token);
      this.fetchCurrentUser().subscribe();
    }
  }

  login(email: string, password: string): Observable<void> {
    const credentials = btoa(`${email}:${password}`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`,
      'Content-Type': 'application/json'
    });

    return this.http.post(`${this.API_URL}/auth/login`, {}, { 
      headers, 
      observe: 'response',
      responseType: 'text'
    }).pipe(
      tap((response: HttpResponse<string>) => {
        const token = response.headers.get(this.TOKEN_KEY);
        if (token) {
          localStorage.setItem(this.TOKEN_KEY, token);
          this.tokenSignal.set(token);
        }
      }),
      tap(() => this.fetchCurrentUser().subscribe()),
      map(() => undefined as void),
      catchError(error => {
        console.error('Login failed:', error);
        throw error;
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.tokenSignal.set(null);
    this.currentUserSignal.set(null);
  }

  private fetchCurrentUser(): Observable<User | null> {
    return this.http.get<User>(`${this.API_URL}/user`).pipe(
      tap(user => this.currentUserSignal.set(user)),
      catchError(() => {
        this.logout();
        return of(null);
      })
    );
  }

  getAuthHeaders(): HttpHeaders {
    const token = this.tokenSignal();
    if (token) {
      return new HttpHeaders({ [this.TOKEN_KEY]: token });
    }
    return new HttpHeaders();
  }

  getToken(): string | null {
    return this.tokenSignal();
  }
}
