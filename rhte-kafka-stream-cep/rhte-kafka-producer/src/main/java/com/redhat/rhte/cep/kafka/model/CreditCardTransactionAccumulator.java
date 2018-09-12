package com.redhat.rhte.cep.kafka.model;

public class CreditCardTransactionAccumulator {

    private String creditCardId;
    private double purchaseTotal;
    private int totalRewardPoints;
    private int currentRewardPoints;
    private int daysFromLastPurchase;

    private CreditCardTransactionAccumulator(String customerId, double purchaseTotal, int rewardPoints) {
        this.creditCardId = customerId;
        this.purchaseTotal = purchaseTotal;
        this.currentRewardPoints = rewardPoints;
        this.totalRewardPoints = rewardPoints;
    }

    public String getCustomerId() {
        return creditCardId;
    }

    public double getPurchaseTotal() {
        return purchaseTotal;
    }

    public int getCurrentRewardPoints() {
        return currentRewardPoints;
    }

    public int getTotalRewardPoints() {
        return totalRewardPoints;
    }

    public void addRewardPoints(int previousTotalPoints) {
        this.totalRewardPoints += previousTotalPoints;
    }

    @Override
    public String toString() {
        return "RewardAccumulator{" +
                "customerId='" + creditCardId + '\'' +
                ", purchaseTotal=" + purchaseTotal +
                ", totalRewardPoints=" + totalRewardPoints +
                ", currentRewardPoints=" + currentRewardPoints +
                ", daysFromLastPurchase=" + daysFromLastPurchase +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCardTransactionAccumulator)) return false;

        CreditCardTransactionAccumulator that = (CreditCardTransactionAccumulator) o;

        if (Double.compare(that.purchaseTotal, purchaseTotal) != 0) return false;
        if (totalRewardPoints != that.totalRewardPoints) return false;
        if (currentRewardPoints != that.currentRewardPoints) return false;
        if (daysFromLastPurchase != that.daysFromLastPurchase) return false;
        return creditCardId != null ? creditCardId.equals(that.creditCardId) : that.creditCardId == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = creditCardId != null ? creditCardId.hashCode() : 0;
        temp = Double.doubleToLongBits(purchaseTotal);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + totalRewardPoints;
        result = 31 * result + currentRewardPoints;
        result = 31 * result + daysFromLastPurchase;
        return result;
    }

    
}
