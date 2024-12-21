package gestiondette.repositories.impl;

import gestiondette.entities.Payment;
import gestiondette.repositories.PaymentRepository;
import gestiondette.repositories.RepositoryImpl;

public class PaymentRepositoryImpl extends RepositoryImpl<Payment> implements PaymentRepository {
    public PaymentRepositoryImpl() {
        super(Payment.class);
    }
}
