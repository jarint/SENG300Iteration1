package com.diy.software;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import com.diy.hardware.BarcodedProduct;
import com.jimmyselectronics.necchi.BarcodeScanner;
import com.jimmyselectronics.necchi.BarcodedItem;


public class addItemWithScanner {

    private BarcodeScanner barcodeScanner;
    private double expectedWeight = 0;
    private double expectedTotalWeight = 0;
    private double currentWeight = 0;
    private double price = 0;
    private boolean isBlocked = false;

    // Exception: The weight in the bagging area does not correspond to expectations; see Weight Discrepancy
    // Exception: An item is scanned when a customer session is not in progress. The scanned information shall simply be ignored.

    /**
     * read barcode and signals this to the system
     * @param item: item scanned through system
     * @return scanned item
     */
    public BarcodedItem detectBarcode(BarcodedItem item) {
        try {
            barcodeScanner.scan(item);
            return item;
        }
        catch (NullPointerSimulationException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * block the self-checkout station from further customer interaction
     */
    public void blockStation() {
        isBlocked = true;
    }

    /**
     * unblock station
     */
    public void unblockStation() {
        isBlocked = false;
    }

    /**
     * Determines the characteristics (weight and cost) of the product associated with the barcode
     * @param item: Barcoded Product
     */
    public void determineItem(BarcodedProduct item) {
        expectedWeight = item.getExpectedWeight();
        price = item.getPrice();
    }

    /**
     * Updates the expected weight from the bagging area
     * @param weight: weight of an item added on weighing area
     */
    public void updateWeight(double weight) {
        currentWeight += weight;
    }

    /**
     * Signals to the customer I/O to place the scanned item in the bagging area
     * @param weight: weight of item scanned
     */
    public void signPlaceItem(double weight) {
        System.out.println("Please put the scanned item in the bagging area");
        if (currentWeight == expectedTotalWeight + weight) {
            expectedTotalWeight += weight;
        }
        else{
            System.out.println("Please put the scanned item in the bagging area");
        }
    }

    /**
     * Signals to the system that the weight has changed
     */
    public void signWeighChange() {
        System.out.println("Weight updated");
    }


}
