package mlm.core;

import mlm.models.Seller;

import java.util.*;
import java.util.stream.Collectors;

public class MLMServiceImpl implements MLMService {
    private Map<String, Seller> sellersByID = new LinkedHashMap<>();
    private Map<String, LinkedHashSet<Seller>> employeesBySellerId = new LinkedHashMap<>();
    private Map<String, Seller> parentBySellerID = new LinkedHashMap<>();
    private Map<String, Integer> salesBySellerId = new LinkedHashMap<>();

    @Override
    public void addSeller(Seller seller) {
        if (this.sellersByID.containsKey(seller.id)) {
            throw new IllegalArgumentException();
        }
        this.sellersByID.put(seller.id, seller);
        this.employeesBySellerId.put(seller.id, new LinkedHashSet<>());
    }

    @Override
    public void hireSeller(Seller parent, Seller newHire) {
        if (!this.sellersByID.containsKey(parent.id) || (this.sellersByID.containsKey(newHire.id))) {
            throw new IllegalArgumentException();
        }
        this.sellersByID.put(newHire.id, newHire);
        this.employeesBySellerId.get(parent.id).add(newHire);
        this.parentBySellerID.put(newHire.id, parent);
    }

    @Override
    public boolean exists(Seller seller) {
        return this.sellersByID.containsKey(seller.id);
    }

    @Override
    public void fire(Seller seller) {
//        this.sellersByID.remove(seller.id);
//
//        Seller a = this.parentBySellerID.remove(seller.id);
//        if (a != null) {
//            LinkedHashSet<Seller> bChildren = this.employeesBySellerId.remove(seller.id);
//            if (bChildren != null) {
//                LinkedHashSet<Seller> oldChildren = this.employeesBySellerId.get(a.id);
//                oldChildren.remove(seller);
//                oldChildren.addAll(bChildren);
//
//                for (Seller bChild : bChildren) {
//                    this.parentBySellerID.put(bChild.id, a);
//                }
//            } else {
//                LinkedHashSet<Seller> oldChildren = this.employeesBySellerId.remove(a.id);
//                oldChildren.remove(seller);
//            }
//        } else {
//            LinkedHashSet<Seller> children = this.employeesBySellerId.get(seller.id);
//            for (Seller child : children) {
//                this.parentBySellerID.put(child.id, null);
//            }
//        }
    }

    @Override
    public void makeSale(Seller seller, int amount) {
        this.salesBySellerId.putIfAbsent(seller.id, 0);
        int oldCount = this.salesBySellerId.get(seller.id);
        this.salesBySellerId.put(seller.id, oldCount + 1);

        Seller parent = this.parentBySellerID.get(seller.id);
        int amout = amount;
        double commision = 0.05 * amount;

        while (parent != null) {
            amout -= commision;
            parent.earnings += commision;
            parent = this.parentBySellerID.get(parent.id);
        }
        seller.earnings += amout;
    }

    @Override
    public Collection<Seller> getByProfits() {
        return this.sellersByID.values()
                .stream()
                .sorted( (first, second) -> {
                    int firstProfirt = first.earnings;
                    int secondProfit = second.earnings;
                    return Integer.compare(secondProfit, firstProfirt);
                })
                .collect(Collectors.toList());
    }
    @Override
    public Collection<Seller> getByEmployeeCount() {
        return this.sellersByID.values()
                .stream()
                .sorted( (first, second) -> {
                    LinkedHashSet<Seller> children1 = this.employeesBySellerId.get(first.id);
                    LinkedHashSet<Seller> children12 = this.employeesBySellerId.get(second.id);
                    return Integer.compare(children12.size(), children1.size());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Seller> getByTotalSalesMade() {
        return this.sellersByID.values()
                .stream()
                .sorted((first, second) -> {
                    int firstCount = this.salesBySellerId.get(first.id);
                    int secondCount = this.salesBySellerId.get(second.id);
                    return Integer.compare(secondCount, firstCount);
                })
                .collect(Collectors.toList());
    }
}
