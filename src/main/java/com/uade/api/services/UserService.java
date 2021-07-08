package com.uade.api.services;

import com.uade.api.entities.Bid;
import com.uade.api.entities.CatalogItem;
import com.uade.api.entities.Picture;
import com.uade.api.entities.Product;
import com.uade.api.entities.User;
import com.uade.api.exceptions.BadRequestException;
import com.uade.api.exceptions.InternalServerException;
import com.uade.api.exceptions.NotFoundException;
import com.uade.api.models.BidModel;
import com.uade.api.models.CategoryType;
import com.uade.api.models.ClientStatus;
import com.uade.api.models.ProductModel;
import com.uade.api.models.ProductStatus;
import com.uade.api.models.StatsModel;
import com.uade.api.models.UserModel;
import com.uade.api.models.UserStatus;
import com.uade.api.repositories.UserRepository;
import com.uade.api.utils.RandomNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final ClientService clientService;
    private final CatalogService catalogService;
    private final ProductService productService;

    public UserService(UserRepository userRepository, ClientService clientService, CatalogService catalogService, ProductService productService) {
        this.userRepository = userRepository;
        this.clientService = clientService;
        this.catalogService = catalogService;
        this.productService = productService;
    }

    @Transactional
    public void registerUser(UserModel userModel) {
        User user = mapModelToEntity(userModel);
        user.setStatus(UserStatus.INACTIVE.value());

        try {
            userRepository.save(user);
            clientService.createClient(user);
        } catch(Exception e) {
            throw new InternalServerException();
        }
    }

    public void updateUser(UserModel userModel) {
        Optional<User> optionalUser = userRepository.findById(userModel.getId());

        optionalUser.ifPresentOrElse(user -> {
            Optional.ofNullable(userModel.getFirstName()).ifPresent(user::setFirstName);
            Optional.ofNullable(userModel.getLastName()).ifPresent(user::setLastName);
            Optional.ofNullable(userModel.getEmail()).ifPresent(user::setEmail);
            Optional.ofNullable(userModel.getAddress()).ifPresent(user::setAddress);
            Optional.ofNullable(userModel.getPhone()).ifPresent(user::setPhone);
            Optional.ofNullable(userModel.getPassword()).ifPresent(user::setPassword);

            userRepository.save(user);
        }, () -> {
            throw new NotFoundException();
        });
    }

    public UserModel getUser(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        return UserModel.builder()
                .id(user.getId())
                .dni(user.getDni())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .status(UserStatus.fromString(user.getStatus()))
                .category(user.getClient() != null ? CategoryType.fromString(user.getClient().getCategory()) : null)
                .clientStatus(user.getClient() != null ? ClientStatus.fromString(user.getClient().getClientStatus()) : null)
                .build();
    }

    public UserModel getUser(Integer userId) {
        return getUser(userId, false);
    }

    public UserModel getUser(Integer userId, boolean addBidInfo) {
        UserModel model = null;
        if (userId != null) {
            User user = userRepository.findById(userId.intValue());
            boolean userHasActiveBid = false;

            if (user == null) {
                throw new NotFoundException();
            }

            if (addBidInfo) {
                userHasActiveBid = user.getClient().getBids()
                        .stream()
                        .anyMatch(bid -> !catalogService.isAuctioned(bid.getCatalog()));
            }

            model = UserModel.builder()
                    .id(user.getId())
                    .dni(user.getDni())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .phone(user.getPhone())
                    .entity(user)
                    .hasActiveBid(userHasActiveBid)
                    .status(UserStatus.fromString(user.getStatus()))
                    .category(CategoryType.fromString(user.getClient().getCategory()))
                    .clientStatus(ClientStatus.fromString(user.getClient().getClientStatus()))
                    .build();
        }

        return model;
    }

    public UserModel getUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("invalid_password");
        }

        return UserModel.builder()
                .id(user.getId())
                .dni(user.getDni())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .status(UserStatus.fromString(user.getStatus()))
                .category(CategoryType.fromString(user.getClient().getCategory()))
                .clientStatus(ClientStatus.fromString(user.getClient().getClientStatus()))
                .build();
    }

    public String updateUserCode(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (ClientStatus.ADMITTED.value().equals(user.getClient().getClientStatus())) {
            throw new BadRequestException("non_admitted_user");
        }

        String newCode = RandomNumberGenerator.generateRandomNumber();

        user.setCode(newCode);

        userRepository.save(user);

        return newCode;
    }

    public void updateUserPassword(String email, String code, String password) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (ClientStatus.ADMITTED.value().equals(user.getClient().getClientStatus()) && user.getCode() == null || !user.getCode().equals(code)) {
            throw new BadRequestException("non_admitted_user_or_invalid_code");
        }

        user.setPassword(password);

        userRepository.save(user);
    }

    public List<BidModel> getBids(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);

        return userOptional
                .map(user -> {
                    List<Bid> bids = user.getClient().getBids();

                    var maxBidByCatalog = bids.stream()
                            .collect(Collectors.groupingBy(b -> b.getCatalog().getId(), Collectors.maxBy(Comparator.comparing(Bid::getAmount))));

                    var maxBids = maxBidByCatalog.values()
                            .stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());


                    return maxBids
                            .stream()
                            .map(this::mapToBidModel)
                            .collect(Collectors.toList());
                        }
                ).orElseThrow(NotFoundException::new);
    }

    public StatsModel getStats(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);

        return userOptional
                .map(user -> {
                    List<Bid> userBids = getBids(user);

                    Function<Bid, CategoryType> bidToCategoryType = bid -> CategoryType.fromString(bid.getCatalog().getAuction().getCategory());

                    Predicate<Bid> isWinningBid = bid -> bid.getCatalog().getBids().stream()
                            .max(Comparator.comparing(Bid::getAmount))
                            .map(winningBid -> winningBid.getId() == bid.getId())
                            .orElseThrow(() -> {
                                return new InternalServerException("error getting winning bid");
                            });

                    Map<CategoryType, Integer> mapPerCategoryType = userBids.stream()
                            .collect(Collectors.groupingBy(bidToCategoryType,
                                    Collectors.collectingAndThen(
                                            Collectors.mapping(bidToCategoryType, Collectors.toSet()),
                                            Set::size)));

                    return StatsModel.builder()
                            .auctionRatio(
                                    StatsModel.AuctionRatioModel.builder()
                                    .won(userBids.stream()
                                            .filter(isWinningBid)
                                            .count()
                                    ).lost(userBids.stream()
                                            .filter(isWinningBid.negate())
                                            .count()
                                    ).build()
                            ).categoryParticipation(mapPerCategoryType.entrySet().stream()
                                    .map(entry -> StatsModel.CategoryParticipationModel.builder()
                                            .value(entry.getValue())
                                            .category(entry.getKey().value())
                                            .build()
                                    ).collect(Collectors.toList())
                            )
                            .build();
                }).orElseThrow(NotFoundException::new);
    }

    public List<ProductModel> getArticles(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);

        return userOptional
                .map(user -> user.getOwner().getProducts()
                        .stream()
                        .map(this::mapToProductModel)
                        .collect(Collectors.toList())
                ).orElseThrow(NotFoundException::new);
    }

    public void approveArticle(Integer id, Integer productId) {
        Optional<User> userOptional = userRepository.findById(id);

        userOptional.ifPresentOrElse(user ->
            user.getOwner().getProducts().stream()
                    .filter(product -> productId.equals(product.getId()))
                    .findFirst()
                    .ifPresentOrElse(productService::approveProduct, () -> {
                        throw new NotFoundException();
                    })
        , () -> {
            throw new NotFoundException();
        });
    }

    public void addArticle(Integer id, ProductModel productModel) {
        Optional<User> userOptional = userRepository.findById(id);

        userOptional.ifPresentOrElse(user -> productService.addProduct(productModel, user.getOwner()), () -> {
            throw new NotFoundException();
        });
    }

    private List<Bid> getBids(User user) {
        List<Bid> bids = user.getClient().getBids();

        var maxBidByCatalog = bids.stream()
                .collect(Collectors.groupingBy(b -> b.getCatalog().getId(), Collectors.maxBy(Comparator.comparing(Bid::getAmount))));

        return maxBidByCatalog.values()
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private BidModel mapToBidModel(Bid bid) {
        Bid winningBid = bid.getCatalog().getBids().stream()
                .max(Comparator.comparing(Bid::getAmount))
                .orElseThrow(InternalServerException::new);
        var bidModelBuilder = BidModel.builder()
                .id(bid.getId())
                .amount(bid.getAmount())
                .createdDate(ZonedDateTime.ofInstant(bid.getDateCreated().toInstant(), ZoneId.systemDefault()))
                .catalogId(bid.getCatalog().getId());

        if (catalogService.isAuctioned(bid.getCatalog())) {
            bidModelBuilder
                    .result(winningBid.getId() == bid.getId() ? "Ganaste" : "Perdiste");
        } else {
            bidModelBuilder
                    .result(winningBid.getId() == bid.getId() ? "Ganando" : "En curso");
        }

        return bidModelBuilder.build();
    }

    private ProductModel mapToProductModel(Product product) {
        //If product has no catalog items it pending approval. We'll assume only 1 item catalog is possible per product
        CatalogItem catalogItem = product.getCatalogItems().stream().findFirst().orElse(null);
        var productModelBuilder = ProductModel.builder()
                .id(product.getId())
                .images(product.getPictures().stream().map(Picture::getPhoto).collect(Collectors.toList()))
                .description(product.getCompleteDescription())
                .fullDescription(product.getCatalogDescription())
                .productStatus(ProductStatus.PENDING_APPROVAL);

        if (catalogItem != null) {
            if ("si".equals(product.getAvailable())) {
                Bid bid = catalogItem.getCatalog().getBids().stream()
                        .max(Comparator.comparing(Bid::getAmount))
                        .orElse(null);

                productModelBuilder
                        .basePrice(catalogItem.getBasePrice())
                        .commission(catalogItem.getComission())
                        .productStatus(ProductStatus.ASSIGNED_AUCTION)
                        .assignedDate(catalogItem.getCatalog().getAuction().getDate())
                        .assignedAuction(catalogItem.getCatalog().getAuction().getId());

                if ("si".equals(catalogItem.getAuctioned()) && bid != null) {
                    productModelBuilder.productStatus(ProductStatus.SOLD)
                            .soldDate(bid.getDateCreated())
                            .soldAmount(bid.getAmount())
                            .earnings(bid.getAmount().subtract(catalogItem.getComission()));
                }
            } else {
                productModelBuilder.productStatus(ProductStatus.PENDING_CONFIRMATION);
            }
        }


        return productModelBuilder.build();
    }

    private User mapModelToEntity(UserModel userModel) {
        User user = new User();

        user.setDni(userModel.getDni());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setPhone(userModel.getPhone());
        user.setAddress(userModel.getAddress());

        return user;
    }
}
