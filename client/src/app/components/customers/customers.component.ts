import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.scss'
})
export class CustomersComponent implements OnInit {
  customers = signal<Customer[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);
  
  editingCustomer = signal<Customer | null>(null);
  newCustomer = signal<Customer | null>(null);

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.loading.set(true);
    this.customerService.getCustomers().subscribe({
      next: (data) => {
        this.customers.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load customers');
        this.loading.set(false);
      }
    });
  }

  startAdd(): void {
    this.newCustomer.set({ firstName: '', lastName: '' });
  }

  cancelAdd(): void {
    this.newCustomer.set(null);
  }

  saveNew(): void {
    const customer = this.newCustomer();
    if (customer && customer.firstName && customer.lastName) {
      this.customerService.saveCustomer(customer).subscribe({
        next: () => {
          this.newCustomer.set(null);
          this.loadCustomers();
        },
        error: () => this.error.set('Failed to save customer')
      });
    }
  }

  startEdit(customer: Customer): void {
    this.editingCustomer.set({ ...customer });
  }

  cancelEdit(): void {
    this.editingCustomer.set(null);
  }

  saveEdit(): void {
    const customer = this.editingCustomer();
    if (customer) {
      this.customerService.saveCustomer(customer).subscribe({
        next: () => {
          this.editingCustomer.set(null);
          this.loadCustomers();
        },
        error: () => this.error.set('Failed to update customer')
      });
    }
  }

  deleteCustomer(id: number): void {
    if (confirm('Are you sure you want to delete this customer?')) {
      this.customerService.deleteCustomer(id).subscribe({
        next: () => this.loadCustomers(),
        error: () => this.error.set('Failed to delete customer')
      });
    }
  }
}
