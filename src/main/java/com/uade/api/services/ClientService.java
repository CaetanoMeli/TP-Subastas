package com.uade.api.services;

import com.uade.api.entities.Client;
import com.uade.api.entities.PaymentMethod;
import com.uade.api.entities.User;
import com.uade.api.exceptions.BadRequestException;
import com.uade.api.exceptions.InternalServerException;
import com.uade.api.exceptions.NotFoundException;
import com.uade.api.models.CategoryType;
import com.uade.api.models.ClientStatus;
import com.uade.api.models.PaymentMethodModel;
import com.uade.api.models.PaymentMethodType;
import com.uade.api.models.UserModel;
import com.uade.api.models.UserStatus;
import com.uade.api.repositories.ClientRepository;
import com.uade.api.repositories.UserRepository;
import com.uade.api.utils.RandomNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final PaymentMethodService paymentMethodService;

    public ClientService(ClientRepository clientRepository, PaymentMethodService paymentMethodService) {
        this.clientRepository = clientRepository;
        this.paymentMethodService = paymentMethodService;
    }

    public Client getClientById(Integer id) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        if (optionalClient.isPresent()) {
            return optionalClient.get();
        } else {
            throw new NotFoundException();
        }
    }

    public List<PaymentMethodModel> getClientPaymentMethods(Integer id) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        return optionalClient.map(client -> {
            List<PaymentMethod> paymentMethods = client.getPaymentMethods();

            return paymentMethods.stream()
                    .map(this::mapToPaymentMethodModel)
                    .collect(Collectors.toList());
        }
        ).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deletePaymentMethod(Integer id, Integer paymentMethodId) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        optionalClient.ifPresentOrElse(client -> {
            List<PaymentMethod> paymentMethods = client.getPaymentMethods();

            PaymentMethod paymentMethodToDelete = paymentMethods.stream()
                    .filter(paymentMethod -> paymentMethodId.equals(paymentMethod.getId()))
                    .findFirst()
                    .orElseThrow(NotFoundException::new);

            paymentMethods.remove(paymentMethodToDelete);
            paymentMethodService.deletePaymentMethod(paymentMethodToDelete);
            }, () -> {
            throw new NotFoundException();
        });
    }

    public void updateUserStatus(Integer id) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        optionalClient.ifPresentOrElse(client -> {
            client.setClientStatus(ClientStatus.ADMITTED.value());
            clientRepository.save(client);
        }, () -> {
            throw new NotFoundException();
        });
    }

    private PaymentMethodModel mapToPaymentMethodModel(PaymentMethod paymentMethod) {
        return PaymentMethodModel.builder()
                .id(paymentMethod.getId())
                .type(PaymentMethodType.fromString(paymentMethod.getType()))
                .number(paymentMethod.getAccountNumber())
                .approved("yes".equals(paymentMethod.getApproved()))
                .company(paymentMethod.getCompany())
                .lastFourDigits(paymentMethod.getLastFourDigits())
                .build();
    }
}
